import static javax.swing.JOptionPane.*
import javax.swing.JOptionPane
import java.util.prefs.Preferences
import groovy.xml.MarkupBuilder
import org.apache.commons.vfs.*
import org.apache.commons.vfs.provider.sftp.*
import com.jcraft.jsch.*

class PreferenceController {

    def prefs = Preferences.userNodeForPackage(PreferenceController)
    def model
    def view

    void mvcGroupInit(Map args) {
        model.preferences = load()
    }

    def load = {
        new XmlParser().parseText(prefs.get('hosts', '<gfiler></gfiler>')).host.collect { it.attributes() }
    }

    def add = {
        def g = buildMVCGroup('OpenFileSystem')
        g.view.panel.createDialog(view.dialog, 'New Host').show()
        if (g.view.panel.value == OK_OPTION) {
            save(model.preferences = model.preferences + g.model.asType(Map))
        }
    }

    def remove = {
        save(model.preferences = model.preferences - view.prefList.selectedValues.asType(List))
    }

    def edit = {
        def host = view.prefList.selectedValue
        def index = view.prefList.selectedIndex
        def g = buildMVCGroup('OpenFileSystem', host:host)
        g.view.panel.createDialog(view.dialog, 'Edit Host').show()
        if (g.view.panel.value == OK_OPTION) {
            model.preferences[index] = g.model.asType(Map)
            save(model.preferences)
            model.preferences = []
            model.preferences = load()
        }
    }

    def open = {
        view.prefList.selectedValues.each { host ->
            def path
            def opts = new FileSystemOptions()
            switch (host.fileSystem) {
                case 'file://':
                    path = "${host.fileSystem}/${host.path}"
                    break
                case 'ftp://':
                case 'sftp://':
                    if (host.keyFilePath) {
                        SftpFileSystemConfigBuilder.instance.with {
                            it.setStrictHostKeyChecking(opts, "no");
                            it.setIdentities(opts, [new File(host.keyFilePath)] as File[])
                            it.setUserInfo(opts, [getPassphrase:{host.passphrase}, promptPassphrase:{true}] as UserInfo)
                        }
                    }
                    path = "${host.fileSystem}${host.username}:${host.password}@${host.host}${host.path}"
                    break
            }
            try {
                app.views.gfiler.rtab.with {
                    it.addTab(host.name, buildMVCGroup('FileList', "FileListView", path:path, opts: opts).view.fileListPane)
                    it.selectedIndex = it.tabCount - 1
                }
                view.dialog.hide()
            } catch (e) {
                JOptionPane.showMessageDialog(view.dialog, e.message, 'Error', ERROR_MESSAGE)
            }
        }
    }

    def save = { hosts ->
        def writer = new StringWriter()
        new MarkupBuilder(writer).gfiler() { hosts.each { host(it) } }
        prefs.put('hosts', writer.toString())
        prefs.flush()
    }
}