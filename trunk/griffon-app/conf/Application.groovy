application {
    title='Gfiler'
    startupGroups = ['gfiler']

    // Should Griffon exit when no Griffon created frames are showing?
    autoShutdown = true

    // If you want some non-standard application class, apply it here
    //frameClass = 'javax.swing.JFrame'
}
mvcGroups {
    // MVC Group for "Preference"
    'Preference' {
        model = 'PreferenceModel'
        controller = 'PreferenceController'
        view = 'PreferenceView'
    }

    // MVC Group for "OpenFileSystem"
    'OpenFileSystem' {
        model = 'OpenFileSystemModel'
        controller = 'OpenFileSystemController'
        view = 'OpenFileSystemView'
    }

    // MVC Group for "FileList"
    'FileList' {
        model = 'FileListModel'
        controller = 'FileListController'
        actions = 'FileListActions'
        view = 'FileListView'
    }

    // MVC Group for "gfiler"
    'gfiler' {
        model = 'GfilerModel'
        controller = 'GfilerController'
        actions = 'GfilerActions'
        view = 'GfilerView'
    }

}
