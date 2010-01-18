import static java.awt.GridBagConstraints.*
import static javax.swing.JOptionPane.*
import static javax.swing.JFileChooser.*

fileChooser(id: 'keyFileChooser', dialogTitle: 'Choose Key File', fileSelectionMode: FILES_ONLY,
    fileHidingEnabled: false, currentDirectory: System.getProperty('user.home') as File)

panel = optionPane(optionType: OK_CANCEL_OPTION, message: panel() {
    def labelGbc = gbc(anchor: EAST)
    def inputGbc = gbc(anchor: WEST, gridwidth: REMAINDER, fill: HORIZONTAL)
    def remoteBind = {
        bind(source: view.fileSystem, sourceEvent: 'itemStateChanged',
            sourceValue: { ['ftp://', 'sftp://'].contains(model.fileSystem) })
    }
    def sftpBind = {
        bind(source: view.fileSystem, sourceEvent: 'itemStateChanged',
            sourceValue: { model.fileSystem == 'sftp://' })
    }

    gridBagLayout()

    label('Name:', constraints: labelGbc)
    textField(id:'name', text: bind(target:model, 'name', value:model.name), columns:30, constraints: inputGbc)

    label('FileSystem:', constraints: labelGbc)
    comboBox(id:'fileSystem', items:['file://', 'ftp://', 'sftp://'],
        selectedItem: bind(target:model, 'fileSystem', value:model.fileSystem), constraints: inputGbc)

    label('Path:', constraints: labelGbc)
    textField(id:'path', text: bind(target:model, 'path', value:model.path), columns:30, constraints: inputGbc)

    label('Host:', constraints: labelGbc, enabled: remoteBind())
    textField(id:'host', text: bind(target:model, 'host', value:model.host), constraints: inputGbc, enabled: remoteBind())

    label('Username:', constraints: labelGbc, enabled: remoteBind())
    textField(id:'username', text: bind(target:model, 'username', value:model.username), constraints: inputGbc, enabled: remoteBind())

    label('Password:', constraints: labelGbc, enabled: remoteBind())
    passwordField(id:'password', text: bind(target:model, 'password', value:model.password), constraints: inputGbc, enabled: remoteBind())

    label('Private Key:', constraints: labelGbc, enabled: sftpBind())
    textField(id:'keyFilePath', columns: 29, text: bind(target:model, 'keyFilePath', value:model.keyFilePath), constraints: gbc(anchor: WEST, fill: HORIZONTAL), enabled: sftpBind())
    button('..', constraints: gbc(anchor: WEST, gridwidth: REMAINDER, fill: HORIZONTAL), enabled: sftpBind(),  actionPerformed: {
        if (keyFileChooser.showOpenDialog(panel) == APPROVE_OPTION) {
            keyFilePath.text = keyFileChooser.selectedFile.absolutePath
        }
    })

    label('Passphrase:', constraints: labelGbc, enabled: sftpBind())
    passwordField(id:'passphrase', text: bind(target:model, 'passphrase', value:model.passphrase), constraints: inputGbc, enabled: sftpBind())

})