package com.najitaka.flipdemo.controller;

import javax.annotation.PostConstruct;

import org.ff4j.FF4j;
import org.ff4j.core.Feature;
import org.ff4j.property.PropertyString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HomeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeController.class);
    private static final String FEATURE_SHOW_WEBCONSOLE = "showWebConsoleURL";
    private static final String FEATURE_SHOW_REST_API   = "showRestApiURL";
    private static final String FEATURE_SHOW_MEI   = "showMEI";
    private static final String PROPERTY_MEI       = "MEI";

    @Autowired
    public FF4j ff4j;

    @PostConstruct
    public void populateDummyFeatureForMySample() {
        if (!ff4j.exist(FEATURE_SHOW_WEBCONSOLE)) {
            ff4j.createFeature(new Feature(FEATURE_SHOW_WEBCONSOLE, true));
        }
        if (!ff4j.exist(FEATURE_SHOW_REST_API)) {
            ff4j.createFeature(new Feature(FEATURE_SHOW_REST_API, true));
        }
        if (!ff4j.exist(FEATURE_SHOW_MEI)) {
            ff4j.createFeature(new Feature(FEATURE_SHOW_MEI, true));
        }
        if (!ff4j.getPropertiesStore().existProperty(PROPERTY_MEI)) {
            ff4j.createProperty(new PropertyString(PROPERTY_MEI, "true"));
        }
        LOGGER.info(" + Features and properties have been created for the demo.");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/html")
    public String get() {
        LOGGER.info(" + Rendering home page...");
        StringBuilder htmlPage = new StringBuilder("<html><body><ul>");
        htmlPage.append("<h2>This page represents a randome page in your application.</h2>");
        htmlPage.append("<p>Each of the list items below is controlled by FF4j.</p>");
        htmlPage.append("<p>If you disable the feature in the web console then the list item will disapear.</p>");
        htmlPage.append("<p>However, the servlet will still respond to the link, since this is just a trick to illustrate FF4J in the demo.</p>");

        htmlPage.append("<h4 style=\"margin: 5px auto;\">List of features:</h4><ul>");

        if (ff4j.check(FEATURE_SHOW_WEBCONSOLE)) {
          htmlPage.append("<li style=\"margin-bottom: 3px;\"> To access the <b>WEB CONSOLE</b> page, ");
          htmlPage.append("please click the link to go to <a href=\"./ff4j-web-console/home\" target=\"_blank\"> ff4j-web-console </a></li>");
        }

        if (ff4j.check(FEATURE_SHOW_REST_API)) {
            htmlPage.append("<li style=\"margin-bottom: 3px;\"> To access the <b>REST API</b> endpoint, ");
            htmlPage.append("please click the link to go to <a href=\"./api/ff4j\" target=\"_blank\">ff4j-rest-api </a>");
        }

        if (ff4j.check(FEATURE_SHOW_MEI)) {
            if (ff4j.getPropertiesStore().existProperty(PROPERTY_MEI)) {
                htmlPage.append("<li style=\"margin-bottom: 3px;\">The " + PROPERTY_MEI + " property is set to ");
                htmlPage.append("<span style=\"color:blue;font-weight:bold\">");
                htmlPage.append(ff4j.getPropertiesStore().readProperty(PROPERTY_MEI).asString());
                htmlPage.append("</span>");
            } else {
                htmlPage.append("<li> The " + PROPERTY_MEI + " property has not been created in FF4J.");
            }
        }
        htmlPage.append("</ul></body></html>");
        return htmlPage.toString();
    }
}