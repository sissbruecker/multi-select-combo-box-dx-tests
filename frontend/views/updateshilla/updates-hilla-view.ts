import { EndpointError } from '@hilla/frontend';
import '@vaadin/button';
import '@vaadin/grid';
import { Grid, GridDataProviderCallback, GridDataProviderParams } from '@vaadin/grid';
import { columnBodyRenderer } from '@vaadin/grid/lit';
import '@vaadin/grid/vaadin-grid-sort-column';
import '@vaadin/horizontal-layout';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/notification';
import { Notification } from '@vaadin/notification';
import '@vaadin/polymer-legacy-adapter';
import '@vaadin/split-layout';
import '@vaadin/vaadin-icons';
import Sort from 'Frontend/generated/dev/hilla/mappedtypes/Sort';
import Direction from 'Frontend/generated/org/springframework/data/domain/Sort/Direction';
import { html, TemplateResult } from 'lit';
import { customElement, property, query } from 'lit/decorators.js';
import { View } from '../view';
import { SoftwareUpdateEndpoint } from 'Frontend/generated/endpoints';
import SoftwareUpdate from 'Frontend/generated/com/example/application/data/entity/SoftwareUpdate';
import 'Frontend/views/updateshilla/software-update-form';
import { SoftwareUpdateForm } from 'Frontend/views/updateshilla/software-update-form';

@customElement('updates-hilla-view')
export class UpdatesHillaView extends View {
  @query('#grid')
  private grid!: Grid;
  @query('software-update-form')
  private form!: SoftwareUpdateForm;

  @property({ type: Number })
  private gridSize = 0;

  private gridDataProvider = this.getGridData.bind(this);

  render() {
    return html`
      <vaadin-split-layout>
        <div class="grid-wrapper">
          <vaadin-grid
            id="grid"
            theme="no-border"
            .size=${this.gridSize}
            .dataProvider=${this.gridDataProvider}
            @active-item-changed=${this.itemSelected}
          >
            <vaadin-grid-sort-column path="version" auto-width></vaadin-grid-sort-column>
            <vaadin-grid-sort-column path="releaseDate" auto-width></vaadin-grid-sort-column>
            <vaadin-grid-column
              auto-width
              header="Models"
              ${columnBodyRenderer(UpdatesHillaView.modelColumnRenderer)}
            ></vaadin-grid-column>
          </vaadin-grid>
        </div>
        <div class="editor-layout">
          <div class="editor">
            <software-update-form></software-update-form>
          </div>
          <vaadin-horizontal-layout class="button-layout">
            <vaadin-button theme="primary" @click=${this.save}>Save</vaadin-button>
            <vaadin-button theme="tertiary" @click=${this.cancel}>Cancel</vaadin-button>
          </vaadin-horizontal-layout>
        </div>
      </vaadin-split-layout>
    `;
  }

  private async getGridData(
    params: GridDataProviderParams<SoftwareUpdate>,
    callback: GridDataProviderCallback<SoftwareUpdate | undefined>
  ) {
    const sort: Sort = {
      orders: params.sortOrders.map((order) => ({
        property: order.path,
        direction: order.direction == 'asc' ? Direction.ASC : Direction.DESC,
        ignoreCase: false,
      })),
    };
    const data = await SoftwareUpdateEndpoint.list({ pageNumber: params.page, pageSize: params.pageSize, sort });
    callback(data!);
  }

  async connectedCallback() {
    super.connectedCallback();
    this.gridSize = (await SoftwareUpdateEndpoint.count()) ?? 0;
  }

  private async itemSelected(event: CustomEvent) {
    const item: SoftwareUpdate = event.detail.value as SoftwareUpdate;
    this.grid.selectedItems = item ? [item] : [];

    if (item) {
      const fromBackend = await SoftwareUpdateEndpoint.get(item.id!);
      fromBackend ? this.form.edit(fromBackend) : this.refreshGrid();
    } else {
      this.form.clear();
    }
  }

  private async save() {
    try {
      const softwareUpdate = await this.form.submit();

      if (softwareUpdate) {
        this.gridSize = (await SoftwareUpdateEndpoint.count()) ?? 0;
        this.form.clear();
        this.refreshGrid();
        Notification.show(`Software update details stored.`, { position: 'bottom-start' });
      }
    } catch (error: any) {
      if (error instanceof EndpointError) {
        Notification.show(`Server error. ${error.message}`, { theme: 'error', position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private cancel() {
    this.grid.activeItem = undefined;
  }

  private refreshGrid() {
    this.grid.selectedItems = [];
    this.grid.clearCache();
  }

  private static modelColumnRenderer(softwareUpdate: SoftwareUpdate): TemplateResult {
    return html`${softwareUpdate.models?.map((model) => model!.name).join(', ')}`;
  }
}
