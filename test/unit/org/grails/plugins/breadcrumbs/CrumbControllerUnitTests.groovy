package org.grails.plugins.breadcrumbs

import grails.test.*
import org.grails.plugins.breadcrumbs.Crumb
import org.grails.plugins.breadcrumbs.CrumbController
import org.junit.Test
import org.codehaus.groovy.grails.commons.GrailsApplication

import groovy.mock.interceptor.MockFor

class CrumbControllerUnitTests extends ControllerUnitTestCase {

    protected void setUp() {
        super.setUp()
    }

    @Test
    void testAddToEmptyCrumbtrail() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(4) { ->
            new ConfigObject()
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def name = "test"
        def href = "http://www.test.com"
        controller.params.name = name
        controller.params.href = href
        
        // when
        def model = controller.add()

        // then
        def newCrumb = new Crumb(name: name, href: href)
        assertEquals([newCrumb], controller.session.crumbtrail)
        assertEquals([newCrumb], model.crumbtrail)
    }

    @Test
    void testAddToCurrentCrumbtrail() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(4) { ->
            new ConfigObject()
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def crumb = new Crumb(name: "first", href:"http://www.first.com")
        controller.session.crumbtrail = [crumb]
        def name = "test"
        def href = "http://www.test.com"
        controller.params.name = name
        controller.params.href = href

        // when
        def model = controller.add()

        // then
        def newCrumb = new Crumb(name: name, href: href)
        assertEquals([crumb, newCrumb], controller.session.crumbtrail)
        assertEquals([crumb, newCrumb], model.crumbtrail)
    }

    @Test
    void testAddToCrumbtrail_maxCrumbsExceeded() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(4) { ->
            def config = new ConfigObject()
            config.breadcrumbs.crumbs.max = 1
            return config
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def crumb = new Crumb(name: "first", href: "http://first.com")
        controller.session.crumbtrail = [crumb]
        def name = "second"
        def href = "http://second.com"
        controller.params.name = name
        controller.params.href = href

        // when
        def model = controller.add()

        // then
        def newCrumb = new Crumb(name: name, href: href)
        assertEquals([newCrumb], controller.session.crumbtrail)
        assertEquals([newCrumb], model.crumbtrail)
    }

    @Test
    void testAddToCrumbtrail_addDuplicates() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(5) { ->
            def config = new ConfigObject()
            config.breadcrumbs.crumbs.showDuplicates="true"
            return config
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def crumb = new Crumb(name: "crumb", href: "http://crumb.com")
        controller.session.crumbtrail = [crumb]
        controller.params.name = crumb.name
        controller.params.href = crumb.href

        // when
        def model = controller.add()

        // then
        assertEquals 2, controller.renderArgs.model.crumbtrail.size()
        assertEquals crumb, controller.renderArgs.model.crumbtrail.first()
        assertEquals crumb, controller.renderArgs.model.crumbtrail.last()
    }

    @Test
    void testAddToCrumbtrail_maxNameLengthExceeded() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(4) { ->
            def config = new ConfigObject()
            config.breadcrumbs.crumbs.maxNameLength = 4
            return config
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def name = "12345"
        def href = "http://www.12345.com"
        controller.params.name = name
        controller.params.href = href

        // when
        def model = controller.add()

        // then
        assertEquals 1, controller.renderArgs.model.crumbtrail.size()
        assertEquals('1234...', controller.renderArgs.model.crumbtrail.first().name)
    }

    @Test
    void testListEmptyCrumbtrail() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(2) { ->
            return new ConfigObject()
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def crumb = new Crumb(name: "first", href:"http://www.first.com")
        controller.session.crumbtrail = [crumb]

        // when
        def model = controller.list()

        // then
        assertEquals([crumb], controller.session.crumbtrail)
        assertEquals([crumb], model.crumbtrail)
    }

    @Test
    void testListCurrentCrumbtrail() {
        // given
        mockController(CrumbController)
        def grailsApp = new MockFor(GrailsApplication)
        grailsApp.demand.getConfig(2) { ->
            return new ConfigObject()
        }
        controller.grailsApplication = grailsApp.proxyInstance()
        def crumb = new Crumb(name: "first", href:"http://www.first.com")
        controller.session.crumbtrail = [crumb]

        // when
        def model = controller.list()

        // then
        assertEquals([crumb], controller.session.crumbtrail)
        assertEquals([crumb], model.crumbtrail)
    }
}
