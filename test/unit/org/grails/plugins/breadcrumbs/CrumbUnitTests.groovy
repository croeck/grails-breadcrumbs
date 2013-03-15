package org.grails.plugins.breadcrumbs

import org.grails.plugins.breadcrumbs.Crumb
import org.junit.Test
import grails.test.GrailsUnitTestCase

class CrumbUnitTests extends GrailsUnitTestCase {

    @Test
    void testEquals_DifferentClass() {
        def crumb1 = new Crumb(name:'name', href:'href')
        def crumb2 = "not a crumb"

        assertFalse crumb1.equals(crumb2)
    }

    @Test
    void testEquals_DifferentName() {
        def crumb1 = new Crumb(name:'name1', href:'href')
        def crumb2 = new Crumb(name:'name2', href:'href')

        assertFalse crumb1.equals(crumb2)
    }

    @Test
    void testEquals_DifferentHref() {
        def crumb1 = new Crumb(name:'name', href:'href1')
        def crumb2 = new Crumb(name:'name', href:'href2')

        assertFalse(crumb1.equals(crumb2))
    }

    @Test
    void testEquals() {
        def crumb1 = new Crumb(name:'name', href:'href')
        def crumb2 = new Crumb(name:'name', href:'href')

        assertTrue crumb1.equals(crumb2)
    }
}
