import static org.apache.commons.vfs.FileType.*
import static java.awt.BorderLayout.*

import java.awt.dnd.*
import java.awt.datatransfer.*
import javax.activation.*
import javax.swing.*
import groovy.ui.Console
import org.apache.commons.vfs.*

static final RENDERER = new DefaultListCellRenderer()
static final FOLDER_ICON = UIManager.getIcon('FileView.directoryIcon')
static final FILE_ICON = UIManager.getIcon('FileView.fileIcon')

class XTransferHandler extends TransferHandler {

    def action

    def localObjectFlavor = new ActivationDataFlavor(Object[], DataFlavor.javaJVMLocalObjectMimeType, "Array of items")

    protected Transferable createTransferable(JComponent c) {
        new DataHandler(c.selectedValues, localObjectFlavor.getMimeType())
    }

    public boolean canImport(TransferHandler.TransferSupport info) {
        if (!info.isDrop() || !info.isDataFlavorSupported(localObjectFlavor)) {
            return false;
        }
        return true
    }

    public int getSourceActions(JComponent c) {
        return TransferHandler.COPY;
    }

    public boolean importData(TransferHandler.TransferSupport info) {
        if (!canImport(info)) {
            return false;
        }
        try {
            action.closure(info.getTransferable().getTransferData(localObjectFlavor))
            return true
        } catch (e) {
            e.printStackTrace()
        }
        return false;
    }
}

popupMenu(id: 'popup') {
    menuItem(deleteAction)
}

fileListPane = scrollPane {
    list = list(id: 'list', listData: bind {model.files}, dragEnabled: true,
        transferHandler: new XTransferHandler(action: copyAction),
        mousePressed: { e ->
            if (e.isPopupTrigger()) {
                popup.show(e.source, e.x, e.y)
            }
            else if (e.clickCount == 2 && e.source.selectedValue && e.source.selectedValue.isDirectory) {
                controller.moveTo(e.source.selectedValue.path)
                e.source.clearSelection()
            }
        },
        cellRenderer: { list, file, index, isSelected, hasFocus ->
            def label = RENDERER.getListCellRendererComponent(list, file.name, index, isSelected, hasFocus)
            label.setIcon(file.isDirectory ? FOLDER_ICON : FILE_ICON); label
        } as ListCellRenderer
    )
}