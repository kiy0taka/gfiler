import static org.apache.commons.vfs.FileType.*
import org.apache.commons.vfs.*
import org.apache.commons.vfs.provider.sftp.*
import javax.swing.*

class FileListController {

    def fsManager
    def model
    def view

    void mvcGroupInit(Map args) {
        fsManager = VFS.getManager()
        if (!fsManager.hasProvider('sftp')) fsManager.addProvider('sftp', new SftpFileProvider())
        moveTo(args.path, args.opts)
    }

    def moveTo(path) {
        moveTo(path, model.opts)
    }

    def moveTo(path, opts) {
        def f = fsManager.resolveFile(path, opts)
        def files = f.parent ? [[name:'..', path:f.parent.URL.toExternalForm(), isDirectory:true]] : []
        f.children.collect {[name:it.name.baseName, path:it.URL.toExternalForm(), isDirectory: it.type == FOLDER]}
            .sort { a, b -> !a.isDirectory <=> !b.isDirectory ?: a.name.toUpperCase() <=> b.name.toUpperCase() }
            .each { files << it }
        model.path = path
        model.opts = opts
        model.files = files
    }

    def copy = { srcFiles ->
        def destFile
        if (view.list.dropLocation.index < 0) {
            destFile = fsManager.resolveFile(model.path)
        } else {
            destFile = fsManager.resolveFile(view.list.model.getElementAt(view.list.dropLocation.index).path)
        }
        destFile = destFile.type == FOLDER ? destFile : destFile.parent
        srcFiles.each {
            def srcFile = fsManager.resolveFile(it.path)
            if (srcFile.parent != destFile) {
                FileUtil.copyContent(srcFile, destFile.resolveFile(srcFile.name.baseName))
            }
        }
        moveTo(model.path)
    }

    def delete = { e ->
        def yesToAll
        def noToAll
        def forceDelete
        String[] options = ['No to All', 'No', 'Yes to All', 'Yes']
        view.list.selectedValues.each {
            def option = yesToAll ? 'Yes to All' : noToAll ? 'No to All' :
                options[view.builder.optionPane().showOptionDialog(
                    app.appFrames[0],
                    'Delete selected file(s) ?',
                    'Confirm',
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    null
                )]
            switch (option) {    
                case 'Yes to All': yesToAll = true
                case 'Yes': forceDelete = true
                    break;
                case 'No to All': noToAll = true
                case 'No': forceDelete = false
                    break;
            }
            if (forceDelete) {
                fsManager.resolveFile(it.path).delete()
            }
        }
        moveTo(model.path)
    }
}