package play

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull


class PlayPublisherPluginTest {

    @Test(expected = IllegalStateException.class)
    public void testThrowsOnLibraryProjects() {
        Project project = ProjectBuilder.builder().build()
        project.apply plugin: 'android-library'
        project.apply plugin: 'play'
    }

    @Test
    public void testCreatesDefaultTask() {
        Project project = evaluatableProject()
        project.evaluate()

        assertNotNull(project.tasks.publishRelease)
        assertEquals(project.tasks.publishRelease.inputFile, project.tasks.zipalignRelease.outputFile)

        assertEquals(project.tasks.zipalignRelease.outputFile, project.tasks.publishRelease.inputFile)
    }

    @Test
    public void testCreatesFlavorTasks() {
        Project project = evaluatableProject()

        project.android.productFlavors {
            free { applicationId( "x.y.z") }
            paid
        }

        project.evaluate()

        assertNotNull(project.tasks.publishPaidRelease)
        assertNotNull(project.tasks.publishFreeRelease)

        assertEquals(project.tasks.zipalignPaidRelease.outputFile, project.tasks.publishPaidRelease.inputFile)
        assertEquals(project.tasks.zipalignFreeRelease.outputFile, project.tasks.publishFreeRelease.inputFile)
    }

    @Test
    public void testDefaultTrack() {
        Project project = evaluatableProject()
        project.evaluate()

        assertEquals('alpha', project.extensions.findByName("play").track)
    }

    @Test
    public void testTrack() {
        Project project = evaluatableProject()

        project.play {
            track 'production'
        }

        project.evaluate()

        assertEquals('production', project.extensions.findByName("play").track)
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTrack() {
        Project project = evaluatableProject()

        project.play {
            track 'gamma'
        }
    }

    def evaluatableProject() {
        Project project = ProjectBuilder.builder().withProjectDir(new File("src/test/fixtures/android_app")).build();
        project.apply plugin: 'android'
        project.apply plugin: 'play'
        project.android {
            compileSdkVersion 20
            buildToolsVersion '20.0.0'

            buildTypes {
                release {
                    signingConfig signingConfigs.debug
                }
            }
        }

        return project
    }
}
