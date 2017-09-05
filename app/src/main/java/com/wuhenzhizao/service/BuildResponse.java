package com.wuhenzhizao.service;

/**
 * Created by wuhenzhizao on 2017/8/23.
 */
public class BuildResponse {
    private int id;
    private VariablesBean variables;

    public int getId() {
        return this.id;
    }

    public VariablesBean getVariables() {
        return this.variables;
    }

    public void setId(int paramInt) {
        this.id = paramInt;
    }

    public void setVariables(VariablesBean paramVariablesBean) {
        this.variables = paramVariablesBean;
    }

    public static class VariablesBean {
        private String ONLINE_BUILD;
        private String PRE_ONLINE_BUILD;

        public String getONLINE_BUILD() {
            return this.ONLINE_BUILD;
        }

        public String getPRE_ONLINE_BUILD() {
            return this.PRE_ONLINE_BUILD;
        }

        public void setONLINE_BUILD(String paramString) {
            this.ONLINE_BUILD = paramString;
        }

        public void setPRE_ONLINE_BUILD(String paramString) {
            this.PRE_ONLINE_BUILD = paramString;
        }
    }
}
