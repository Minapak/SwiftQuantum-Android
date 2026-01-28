package com.swiftquantum.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swiftquantum.domain.model.Circuit
import com.swiftquantum.domain.model.ExportOptions
import com.swiftquantum.domain.model.ImportResult
import com.swiftquantum.domain.model.QASMCircuit
import com.swiftquantum.domain.model.QASMTemplate
import com.swiftquantum.domain.model.QASMTemplateCategory
import com.swiftquantum.domain.model.QASMValidationResult
import com.swiftquantum.domain.model.QASMVersion
import com.swiftquantum.domain.usecase.ExportQASMUseCase
import com.swiftquantum.domain.usecase.ImportQASMUseCase
import com.swiftquantum.domain.usecase.LoadQASMTemplatesUseCase
import com.swiftquantum.domain.usecase.ValidateQASMUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QASMUiState(
    val code: String = "",
    val selectedVersion: QASMVersion = QASMVersion.QASM2,
    val isImporting: Boolean = false,
    val isExporting: Boolean = false,
    val isValidating: Boolean = false,
    val importedCircuit: Circuit? = null,
    val qasmCircuit: QASMCircuit? = null,
    val exportedCode: String? = null,
    val validationResult: QASMValidationResult? = null,
    val templates: List<QASMTemplate> = emptyList(),
    val selectedCategory: QASMTemplateCategory? = null,
    val isLoadingTemplates: Boolean = false,
    val error: String? = null,
    val includeComments: Boolean = true,
    val prettyPrint: Boolean = true,
    val includeMeasurements: Boolean = true
)

sealed class QASMEvent {
    data class ImportSuccess(val circuit: Circuit) : QASMEvent()
    data class ExportSuccess(val code: String) : QASMEvent()
    data class ValidationComplete(val isValid: Boolean) : QASMEvent()
    data class Error(val message: String) : QASMEvent()
    data object CodeCopied : QASMEvent()
}

@HiltViewModel
class QASMViewModel @Inject constructor(
    private val importQASMUseCase: ImportQASMUseCase,
    private val exportQASMUseCase: ExportQASMUseCase,
    private val validateQASMUseCase: ValidateQASMUseCase,
    private val loadQASMTemplatesUseCase: LoadQASMTemplatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QASMUiState())
    val uiState: StateFlow<QASMUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<QASMEvent>()
    val events: SharedFlow<QASMEvent> = _events.asSharedFlow()

    init {
        loadTemplates()
    }

    fun updateCode(code: String) {
        _uiState.value = _uiState.value.copy(
            code = code,
            validationResult = null,
            error = null
        )
    }

    fun setVersion(version: QASMVersion) {
        _uiState.value = _uiState.value.copy(selectedVersion = version)
    }

    fun setIncludeComments(include: Boolean) {
        _uiState.value = _uiState.value.copy(includeComments = include)
    }

    fun setPrettyPrint(prettyPrint: Boolean) {
        _uiState.value = _uiState.value.copy(prettyPrint = prettyPrint)
    }

    fun setIncludeMeasurements(include: Boolean) {
        _uiState.value = _uiState.value.copy(includeMeasurements = include)
    }

    fun importQASM() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isImporting = true, error = null)

            importQASMUseCase(
                code = _uiState.value.code,
                version = _uiState.value.selectedVersion
            ).onSuccess { result ->
                if (result.success && result.circuit != null) {
                    _uiState.value = _uiState.value.copy(
                        isImporting = false,
                        importedCircuit = result.circuit,
                        qasmCircuit = result.qasmCircuit,
                        error = null
                    )
                    _events.emit(QASMEvent.ImportSuccess(result.circuit))
                } else {
                    _uiState.value = _uiState.value.copy(
                        isImporting = false,
                        error = result.errors.firstOrNull() ?: "Import failed"
                    )
                    _events.emit(QASMEvent.Error(result.errors.firstOrNull() ?: "Import failed"))
                }
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isImporting = false,
                    error = error.message
                )
                _events.emit(QASMEvent.Error(error.message ?: "Import failed"))
            }
        }
    }

    fun exportCircuit(circuit: Circuit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isExporting = true, error = null)

            val options = ExportOptions(
                version = _uiState.value.selectedVersion,
                includeComments = _uiState.value.includeComments,
                prettyPrint = _uiState.value.prettyPrint,
                includeMeasurements = _uiState.value.includeMeasurements
            )

            exportQASMUseCase(circuit, options).onSuccess { (code, qasmCircuit) ->
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    exportedCode = code,
                    code = code,
                    qasmCircuit = qasmCircuit,
                    error = null
                )
                _events.emit(QASMEvent.ExportSuccess(code))
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isExporting = false,
                    error = error.message
                )
                _events.emit(QASMEvent.Error(error.message ?: "Export failed"))
            }
        }
    }

    fun validateCode() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isValidating = true, error = null)

            validateQASMUseCase(_uiState.value.code).onSuccess { result ->
                _uiState.value = _uiState.value.copy(
                    isValidating = false,
                    validationResult = result,
                    error = if (!result.isValid) result.errors.firstOrNull()?.message else null
                )
                _events.emit(QASMEvent.ValidationComplete(result.isValid))
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isValidating = false,
                    error = error.message
                )
                _events.emit(QASMEvent.Error(error.message ?: "Validation failed"))
            }
        }
    }

    fun loadTemplates(category: QASMTemplateCategory? = null) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingTemplates = true,
                selectedCategory = category
            )

            loadQASMTemplatesUseCase(category).onSuccess { templates ->
                _uiState.value = _uiState.value.copy(
                    isLoadingTemplates = false,
                    templates = templates
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    isLoadingTemplates = false,
                    error = error.message
                )
            }
        }
    }

    fun selectTemplate(template: QASMTemplate) {
        _uiState.value = _uiState.value.copy(
            code = template.code,
            selectedVersion = QASMVersion.QASM2, // Templates are in QASM2 format
            validationResult = null,
            error = null
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearImportedCircuit() {
        _uiState.value = _uiState.value.copy(
            importedCircuit = null,
            qasmCircuit = null
        )
    }

    fun clearExportedCode() {
        _uiState.value = _uiState.value.copy(exportedCode = null)
    }

    fun onCodeCopied() {
        viewModelScope.launch {
            _events.emit(QASMEvent.CodeCopied)
        }
    }
}
