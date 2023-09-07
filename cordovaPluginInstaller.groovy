@Grab(group = 'org.eclipse.jgit', module = 'org.eclipse.jgit', version = '5.11.0.202103091610-r')
import org.eclipse.jgit.api.Git
import groovy.xml.*

addPermissionsToManifest(pluginXml, androidProjectPath + 'app/src/main/AndroidManifest.xml')
        {
            def manifestFile = new File(manifestPath)
            def manifest = new XmlSlurper().parse(manifestFile)

            println "Using plugin XML at: ${pluginXmlPath}"
            println "Android project path: ${androidProjectPath}"

            pluginXml.'config-file'.findAll { it.@target == 'AndroidManifest.xml' && it.@parent == '/*' }.each { configFile ->
                configFile.'uses-permission'.each { permission ->
                    def permAttributes = permission.attributes()
                    def permissionNode = new Node(null, 'uses-permission', permAttributes)
                    manifest.appendNode(permissionNode)
                }
            }

            // Save the updated manifest
            def printer = new XmlNodePrinter(new PrintWriter(manifestFile))
            printer.preserveWhitespace = true
            printer.print(manifest)
        }

def downloadPluginFromGitHub(repoURL, destinationDir) {
    Git.cloneRepository()
            .setURI(repoURL)
            .setDirectory(new File(destinationDir))
            .call()
}

def applyPluginConfigurations(pluginXmlPath, androidProjectPath) {
    def pluginXml = new XmlParser().parse(pluginXmlPath)
    addPermissionsToManifest(pluginXml, androidProjectPath + 'AndroidManifest.xml')
    // 根据plugin.xml内容，更新Android配置文件。这里你需要根据plugin.xml中的内容写逻辑。
    // 例如，你可能需要添加新的权限到AndroidManifest.xml，或者更新build.gradle等。
}

// 使用方法
def repoURL = "https://github.com/apache/cordova-plugin-camera.git"
def destinationDir = "./cordovaPlugins" // 在Android项目中的目标目录

downloadPluginFromGitHub(repoURL, destinationDir)
applyPluginConfigurations(destinationDir + "/plugin.xml", "./") // 假设你的Android项目根目录在当前目录
