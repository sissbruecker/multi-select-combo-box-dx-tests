package com.example.application.views.updates;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.SoftwareUpdateService;
import com.example.application.data.service.TeslaModelService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Updates")
@Route(value = "updates/:softwareUpdateID?/:action?(edit)")
@RouteAlias(value = "")
@Uses(Icon.class)
public class UpdatesView extends Div implements BeforeEnterObserver {

    private final String SOFTWARE_UPDATE_ID = "softwareUpdateID";
    private final String SOFTWARE_UPDATE_EDIT_ROUTE_TEMPLATE = "updates/%s/edit";

    private Grid<SoftwareUpdate> grid = new Grid<>(SoftwareUpdate.class, false);

    private TextField version;
    private DatePicker releaseDate;
    private MultiSelectComboBox<TeslaModel> models;

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private BeanValidationBinder<SoftwareUpdate> binder;

    private SoftwareUpdate softwareUpdate;

    private final SoftwareUpdateService softwareUpdateService;
    private TeslaModelService teslaModelService;

    @Autowired
    public UpdatesView(SoftwareUpdateService softwareUpdateService, TeslaModelService teslaModelService) {
        this.softwareUpdateService = softwareUpdateService;
        this.teslaModelService = teslaModelService;
        addClassNames("updates-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("version").setAutoWidth(true);
        grid.addColumn("releaseDate").setAutoWidth(true);
        grid.addColumn(new TextRenderer<>(update -> update.getModels().stream().map(TeslaModel::getName).collect(Collectors.joining(", ")))).setHeader("Models").setAutoWidth(true);

        grid.setItems(query -> softwareUpdateService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(SOFTWARE_UPDATE_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(UpdatesView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(SoftwareUpdate.class);

        // Bind fields. This is where you'd define e.g. validation rules

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.softwareUpdate == null) {
                    this.softwareUpdate = new SoftwareUpdate();
                }
                binder.writeBean(this.softwareUpdate);

                softwareUpdateService.update(this.softwareUpdate);
                clearForm();
                refreshGrid();
                Notification.show("Software updates details stored.");
                UI.getCurrent().navigate(UpdatesView.class);
            } catch (ValidationException validationException) {
                Notification.show("An exception happened while trying to store the software updates details.");
            }
        });

    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<UUID> softwareUpdateId = event.getRouteParameters().get(SOFTWARE_UPDATE_ID).map(UUID::fromString);
        if (softwareUpdateId.isPresent()) {
            Optional<SoftwareUpdate> softwareUpdateFromBackend = softwareUpdateService.get(softwareUpdateId.get());
            if (softwareUpdateFromBackend.isPresent()) {
                populateForm(softwareUpdateFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested software upate was not found, ID = %s", softwareUpdateId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(UpdatesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        version = new TextField("Version");
        releaseDate = new DatePicker("Release Date");
        models = new MultiSelectComboBox<>("Models");
        models.setItemLabelGenerator(TeslaModel::getName);
        models.setItems(teslaModelService.listAll());

        Component[] fields = new Component[]{version, releaseDate, models};

        formLayout.add(fields);
        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getLazyDataView().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(SoftwareUpdate value) {
        this.softwareUpdate = value;
        binder.readBean(this.softwareUpdate);
    }
}
