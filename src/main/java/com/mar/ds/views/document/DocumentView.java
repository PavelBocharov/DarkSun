package com.mar.ds.views.document;

import com.mar.ds.db.entity.Document;
import com.mar.ds.utils.DeleteDialogWidget;
import com.mar.ds.utils.ViewUtils;
import com.mar.ds.views.ContentView;
import com.mar.ds.views.MainView;
import com.mar.ds.views.document.status.DocumentStatusViewDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.logging.Logger;

import static com.vaadin.flow.component.button.ButtonVariant.LUMO_TERTIARY;
import static com.vaadin.flow.component.icon.VaadinIcon.*;
import static java.lang.String.format;

@RequiredArgsConstructor
public class DocumentView implements ContentView {

    private Logger logger = Logger.getLogger(DocumentView.class.getSimpleName());

    private final MainView appLayout;

    public VerticalLayout getContent() {
        H2 label = new H2("Список книг и документов");
        // TABLE
        Grid<Document> grid = new Grid<>();

        // column
        grid.addColumn(Document::getId).setHeader("ID").setAutoWidth(true);
        grid.addColumn(Document::getTitle).setHeader("Заголовок").setAutoWidth(true);
        grid.addColumn(document -> format("%32s", document.getText())).setHeader("Текст").setAutoWidth(true);
        grid.addColumn(document -> format("%32s", document.getBtnTitle())).setHeader("Заголовок кнопки").setAutoWidth(true);
        grid.addColumn(document -> format("%32s", document.getImage())).setHeader("Путь изображения").setAutoWidth(true);
        grid.addColumn(
                        document -> format("[%d] %32s", document.getDocumentStatus().getEnumId(), document.getDocumentStatus().getTitle())
                )
                .setHeader("Статус").setAutoWidth(true)
        ;
        // settings
        grid.setWidthFull();
        grid.addThemeVariants(GridVariant.LUMO_COMPACT);
//         edit
        grid.addItemDoubleClickListener(
                dialogItemDoubleClickEvent -> new UpdateDocumentView(appLayout, dialogItemDoubleClickEvent.getItem())
        );
        grid.addComponentColumn(document -> {
            Button edtBtn = new Button(new Icon(VaadinIcon.PENCIL), clk -> {
                new UpdateDocumentView(appLayout, document);
            });
//            edtBtn.addThemeVariants(LUMO_TERTIARY);
            Button dltBtn = new Button(new Icon(BAN), clk -> {
                new DeleteDialogWidget(() -> {
                    appLayout.getRepositoryService().getDocumentRepository().delete(document);
                    appLayout.setContent(appLayout.getDocumentView().getContent());
                });
            });
            dltBtn.addThemeVariants(LUMO_TERTIARY);
            dltBtn.getStyle().set("color", "red");
            return new HorizontalLayout(
                    edtBtn, dltBtn
            );
        });

        // value
        List<Document> documents = appLayout.getRepositoryService().getDocumentRepository().findAll();
        grid.setItems(documents);

        // down buttons
        Button crtBtn = new Button("Добавить документ", new Icon(PLUS), click -> new CreateDocumentView(appLayout));
        crtBtn.setWidthFull();
        crtBtn.getStyle().set("color", "green");


        Button documentStatusView = new Button("Статус документов", new Icon(COG), click -> new DocumentStatusViewDialog(appLayout));
        documentStatusView.setWidthFull();

        HorizontalLayout btns = new HorizontalLayout(
                crtBtn,
                documentStatusView,
                ViewUtils.getDownloadFileButton("Documents.json",
                        appLayout.getMapperService().getDocumentMapper().mappingDocuments(
                                appLayout.getRepositoryService().getDocumentRepository().findAll()
                        )
                )
        );
        btns.setWidthFull();

        // create view
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setSizeFull();
        verticalLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.START, label);
        verticalLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.CENTER, grid);
        verticalLayout.setHorizontalComponentAlignment(FlexComponent.Alignment.END, btns);
        verticalLayout.add(label, grid, btns);
        return verticalLayout;
    }
}
