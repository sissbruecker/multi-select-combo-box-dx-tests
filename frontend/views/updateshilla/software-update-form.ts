import {html, LitElement} from "lit";
import {customElement} from "lit/decorators.js";
import {Binder, field} from "@hilla/form";
import '@vaadin/date-picker';
import '@vaadin/form-layout';
import '@vaadin/multi-select-combo-box';
import {MultiSelectComboBoxSelectedItemsChangedEvent} from "@vaadin/multi-select-combo-box";
import '@vaadin/text-field';
import SoftwareUpdate from "Frontend/generated/com/example/application/data/entity/SoftwareUpdate";
import SoftwareUpdateModel from "Frontend/generated/com/example/application/data/entity/SoftwareUpdateModel";
import {state} from "lit/decorators";
import TeslaModel from "Frontend/generated/com/example/application/data/entity/TeslaModel";
import {SoftwareUpdateEndpoint, TeslaModelEndpoint} from "Frontend/generated/endpoints";

@customElement('software-update-form')
export class SoftwareUpdateForm extends LitElement {
    private binder = new Binder<SoftwareUpdate, SoftwareUpdateModel>(this, SoftwareUpdateModel);

    @state()
    private availableModels: TeslaModel[] = [];
    @state()
    private selectedModels: TeslaModel[] = [];

    async connectedCallback() {
        super.connectedCallback();
        this.availableModels = await TeslaModelEndpoint.listAll();
    }

    edit(softwareUpdate: SoftwareUpdate) {
        this.binder.read(softwareUpdate);
        this.selectedModels = [...softwareUpdate.models];
    }

    async submit(): Promise<SoftwareUpdate | void> {
        return this.binder.submitTo(softwareUpdate => {
            softwareUpdate.models = this.selectedModels;
            return SoftwareUpdateEndpoint.update(softwareUpdate);
        });
    }

    clear() {
        this.binder.clear();
        this.selectedModels = [];
    }

    render() {
        return html`
          <vaadin-form-layout>
            <vaadin-text-field
                label="Version"
                id="version"
                ${field(this.binder.model.version)}
            ></vaadin-text-field>
            <vaadin-date-picker
                label="Release Date"
                id="lastName"
                ${field(this.binder.model.releaseDate)}
            ></vaadin-date-picker>
            <vaadin-multi-select-combo-box
                label="Models"
                .items="${this.availableModels}"
                .selectedItems="${this.selectedModels}"
                @selected-items-changed="${(e: MultiSelectComboBoxSelectedItemsChangedEvent<TeslaModel>) => this.selectedModels = e.detail.value}"
                item-id-path="id"
                item-label-path="name"
            ></vaadin-multi-select-combo-box>
          </vaadin-form-layout>
        `;
    }
}