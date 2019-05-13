package com.atlassian.opsgenie.codegen.geniepy;

import io.swagger.codegen.CodegenConfig;
import io.swagger.codegen.CodegenModel;
import io.swagger.codegen.SupportingFile;
import io.swagger.codegen.languages.PythonClientCodegen;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GeniepyGenerator extends PythonClientCodegen implements CodegenConfig {

    private static final String GENIEPY = "geniepy";

    private List<OpsgenieCodegenModel> opsgenieCodegenModels = new LinkedList<>();

    public String getName() {
        return GENIEPY;
    }

    public String getHelp() {
        return "Generates a " + GENIEPY + " client library.";
    }

    public GeniepyGenerator() {
        super();

        outputFolder = "generated-code" + File.separatorChar + GENIEPY;

        embeddedTemplateDir = templateDir = GENIEPY;

        additionalProperties.put("opsgenieCodegenModels", opsgenieCodegenModels);
    }

    @Override
    public void processOpts() {
        super.processOpts();

        supportingFiles.add(new SupportingFile(GENIEPY + File.separatorChar + "opsgenieCodegenModels.mustache", "", "opsgenieCodegenModels.csv"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> postProcessAllModels(Map<String, Object> objs) {
        Map<String, Object> allProcessedModels = super.postProcessAllModels(objs);

        Map<String, OpsgenieCodegenModel> opsgenieDomainsMap = new HashMap<>();

        Set<String> keys = new HashSet<>(allProcessedModels.keySet());
        for (String name : keys) {

            Map<String, Object> allModels = (Map<String, Object>) allProcessedModels.get(name);
            List<Map<String, Object>> models = (List<Map<String, Object>>) allModels.get("models");

            for (Map<String, Object> mo : models) {
                CodegenModel cm = (CodegenModel) mo.get("model");
                OpsgenieCodegenModel ocm;

                String opsgenieDomain = (String) cm.vendorExtensions.get("x-opsgenie-domain");
                if (StringUtils.isNotEmpty(opsgenieDomain)) {
                    ocm = generateOpsgenieCodegenModel(opsgenieDomain, cm);
                } else {
                    ocm = generateOpsgenieCodegenModelFromParentModel(cm, allProcessedModels);
                }

                if (ocm == null) {
                    throw new RuntimeException("Empty x-opsgenie-domain not allowed for models! Model name: " + cm.name);
                }

                opsgenieDomainsMap.put(ocm.getCodegenModel().name, ocm);
            }
        }

        List<OpsgenieCodegenModel> opsgenieDomainMapSorted = opsgenieDomainsMap.values().stream()
            .sorted(Comparator.comparing(ocm -> ocm.getCodegenModel().classname))
            .collect(Collectors.toList());

        opsgenieCodegenModels.addAll(opsgenieDomainMapSorted);

        return allProcessedModels;
    }

    private OpsgenieCodegenModel generateOpsgenieCodegenModel(String opsgenieDomain, CodegenModel codegenModel) {
        return new OpsgenieCodegenModel()
            .setOpsgenieDomain(opsgenieDomain)
            .setCodegenModel(codegenModel);
    }

    @SuppressWarnings("unchecked")
    private OpsgenieCodegenModel generateOpsgenieCodegenModelFromParentModel(CodegenModel codegenModel, Map<String, Object> allProcessedModels) {
        if (!codegenModel.name.contains("_")) {
            return null;
        }

        String modelNameBelongsTo = StringUtils.substringBefore(codegenModel.name, "_");
        if (StringUtils.isEmpty(modelNameBelongsTo)) {
            return null;
        }

        Map<String, Object> allModels = (Map<String, Object>) allProcessedModels.get(modelNameBelongsTo);
        List<Map<String, Object>> models = (List<Map<String, Object>>) allModels.get("models");

        for (Map<String, Object> mo : models) {
            CodegenModel cm = (CodegenModel) mo.get("model");

            String opsgenieDomain = (String) cm.vendorExtensions.get("x-opsgenie-domain");
            if (StringUtils.isNotEmpty(opsgenieDomain)) {
                return generateOpsgenieCodegenModel(opsgenieDomain, codegenModel);
            }
        }

        return null;
    }
}
