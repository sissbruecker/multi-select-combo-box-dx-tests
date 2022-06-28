package com.example.application.views.updates;

import com.example.application.data.entity.SoftwareUpdate;
import com.example.application.data.entity.TeslaModel;
import com.example.application.data.service.SoftwareUpdateService;
import com.example.application.data.service.TeslaModelService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
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

@PageTitle("Tesla Software Updates")
@Route(value = "updates/:softwareUpdateID?/:action?(edit)")
@RouteAlias(value = "")
@Uses(Icon.class)
public class SoftwareUpdatesView extends Div implements BeforeEnterObserver {

    private final String SOFTWARE_UPDATE_ID = "softwareUpdateID";
    private final String SOFTWARE_UPDATE_EDIT_ROUTE_TEMPLATE = "updates/%s/edit";

    private Grid<SoftwareUpdate> grid = new Grid<>(SoftwareUpdate.class, false);

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private final SoftwareUpdateService softwareUpdateService;
    private final TeslaModelService teslaModelService;

    private SoftwareUpdateForm form;

    @Autowired
    public SoftwareUpdatesView(SoftwareUpdateService softwareUpdateService, TeslaModelService teslaModelService) {
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
                form.clear();
                UI.getCurrent().navigate(SoftwareUpdatesView.class);
            }
        });

        cancel.addClickListener(e -> {
            form.clear();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                SoftwareUpdate softwareUpdate = form.submit();

                softwareUpdateService.update(softwareUpdate);
                form.clear();
                refreshGrid();
                Notification.show("Software updates details stored.");
                UI.getCurrent().navigate(SoftwareUpdatesView.class);
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
                form.edit(softwareUpdateFromBackend.get());
            } else {
                Notification.show(
                        String.format("The requested software upate was not found, ID = %s", softwareUpdateId.get()), 3000,
                        Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(SoftwareUpdatesView.class);
            }
        }
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        form = new SoftwareUpdateForm(this.teslaModelService);
        editorDiv.add(form);
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
}
