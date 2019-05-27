package org.openapitools.codegen.debug;

import org.junit.Test;
import org.openapitools.codegen.OpenAPIGenerator;

/***
 * This test allows you to easily launch your code generation software under a debugger.
 * Then run this test under debug mode.  You will be able to step through your java code 
 * and then see the results in the out directory. 
 *
 * To experiment with debugging your code generator:
 * 1) Set a break point in GeniepyGenerator.java in the postProcessOperationsWithModels() method.
 * 2) To launch this test in Eclipse: right-click | Debug As | JUnit Test
 *
 */
public class DebugCodegenLauncher {
    @Test
    public void launchCodeGeneratorInDebugMode() {
        // use this test to launch you code generator in the debugger.
        // this allows you to easily set break points in GeniepyGenerator.
        String commandLineParams =
            "generate " +
                "-i https://raw.githubusercontent.com/openapitools/openapi-generator/master/modules/openapi-generator/src/test/resources/2_0/petstore.yaml " + // sample OpenAPI spec
                "-t ./src/main/resources/geniepy " +          // template directory
                "-o out/geniepy " +                           // output directory
                "-g geniepy ";                               // use this codegen library

        try {
            OpenAPIGenerator.main(commandLineParams.split(" "));
        } catch (Exception | Error ex) {
            System.err.println(ex.toString());
        }
    }
}
