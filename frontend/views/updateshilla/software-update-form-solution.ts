import { html, LitElement } from 'lit';
import { customElement } from 'lit/decorators.js';
import '@vaadin/button';
import '@vaadin/date-picker';
import '@vaadin/form-layout';
import '@vaadin/multi-select-combo-box';
import { MultiSelectComboBoxSelectedItemsChangedEvent } from '@vaadin/multi-select-combo-box';
import '@vaadin/text-area';
import '@vaadin/text-field';
import SoftwareUpdate from 'Frontend/generated/com/example/application/data/entity/SoftwareUpdate';
import { state } from 'lit/decorators';
import TeslaModel from 'Frontend/generated/com/example/application/data/entity/TeslaModel';
import { SoftwareUpdateEndpoint, TeslaModelEndpoint } from 'Frontend/generated/endpoints';
import { TextFieldValueChangedEvent } from '@vaadin/text-field';
import { DatePickerValueChangedEvent } from '@vaadin/date-picker';

@customElement('software-update-form-solution')
export class SoftwareUpdateFormSolution extends LitElement {
  @state()
  private editedSoftwareUpdate!: SoftwareUpdate;

  @state()
  private availableModels: TeslaModel[] = [];

  get selectedModelsText(): string {
    return this.editedSoftwareUpdate.models.map((model) => model.name).join(', ');
  }

  get isSelectAllModelsEnabled(): boolean {
    return this.editedSoftwareUpdate.models.length === 0;
  }

  async connectedCallback() {
    super.connectedCallback();
    this.clear();
    this.availableModels = await TeslaModelEndpoint.listAll();
  }

  edit(softwareUpdate: SoftwareUpdate) {
    this.editedSoftwareUpdate = softwareUpdate;
  }

  async submit() {
    return SoftwareUpdateEndpoint.update(this.editedSoftwareUpdate);
  }

  clear() {
    this.editedSoftwareUpdate = {
      version: '',
      releaseDate: '',
      models: [],
    };
  }

  render() {
    return html`
      <vaadin-form-layout>
        <vaadin-text-field
          label="Version"
          id="version"
          .value="${this.editedSoftwareUpdate.version}"
          @value-changed="${(e: TextFieldValueChangedEvent) =>
            (this.editedSoftwareUpdate = {
              ...this.editedSoftwareUpdate,
              version: e.detail.value,
            })}"
        ></vaadin-text-field>
        <vaadin-date-picker
          label="Release Date"
          id="lastName"
          .value="${this.editedSoftwareUpdate.releaseDate}"
          @value-changed="${(e: DatePickerValueChangedEvent) =>
            (this.editedSoftwareUpdate = {
              ...this.editedSoftwareUpdate,
              releaseDate: e.detail.value,
            })}"
        ></vaadin-date-picker>
        <vaadin-multi-select-combo-box
          label="Models"
          item-id-path="id"
          item-label-path="name"
          .items="${this.availableModels}"
          .selectedItems="${this.editedSoftwareUpdate.models}"
          @selected-items-changed="${(e: MultiSelectComboBoxSelectedItemsChangedEvent<TeslaModel>) =>
            (this.editedSoftwareUpdate = {
              ...this.editedSoftwareUpdate,
              models: e.detail.value,
            })}"
        ></vaadin-multi-select-combo-box>
        <vaadin-text-area label="Selected models" readonly .value="${this.selectedModelsText}"></vaadin-text-area>
        <vaadin-button
          label="Selected models"
          .disabled="${!this.isSelectAllModelsEnabled}"
          .value="${this.selectedModelsText}"
          @click="${(e: Event) =>
            (this.editedSoftwareUpdate = {
              ...this.editedSoftwareUpdate,
              models: [...this.availableModels],
            })}"
          >Select all
        </vaadin-button>
      </vaadin-form-layout>
    `;
  }
}
