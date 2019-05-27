package com.atlassian.opsgenie.codegen.geniepy;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openapitools.codegen.CodegenModel;

import java.util.Objects;

public class OpsgenieCodegenModel {

    private String opsgenieDomain;
    private CodegenModel codegenModel;

    public String getOpsgenieDomain() {
        return opsgenieDomain;
    }

    public OpsgenieCodegenModel setOpsgenieDomain(String opsgenieDomain) {
        this.opsgenieDomain = opsgenieDomain;
        return this;
    }

    public CodegenModel getCodegenModel() {
        return codegenModel;
    }

    public OpsgenieCodegenModel setCodegenModel(CodegenModel codegenModel) {
        this.codegenModel = codegenModel;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpsgenieCodegenModel that = (OpsgenieCodegenModel) o;
        return Objects.equals(opsgenieDomain, that.opsgenieDomain) &&
            Objects.equals(codegenModel, that.codegenModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(opsgenieDomain, codegenModel);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("opsgenieDomain", opsgenieDomain)
            .append("codegenModel", codegenModel)
            .toString();
    }
}
