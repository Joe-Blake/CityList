package com.demo.joe.citylist.bean;

import java.util.List;

/**
 * Created by joe on 2016/12/19.
 */
public class CitiesBean {
    /**
     * openCity : [{"citySet":[{"cityid":1,"firstLetter":"b","alias":"bj","name":"北京"}],
     * "index":"B"},{"citySet":[{"cityid":414,"firstLetter":"c","alias":"cs","name":"长沙"},
     * {"cityid":319,"firstLetter":"c","alias":"cc","name":"长春"},{"cityid":102,"firstLetter":"c",
     * "alias":"cd","name":"成都"},{"cityid":37,"firstLetter":"c","alias":"cq","name":"重庆"}],
     * "index":"C"},{"citySet":[{"cityid":541,"firstLetter":"k","alias":"km","name":"昆明"}],
     * "index":"K"},{"citySet":[{"cityid":1057,"firstLetter":"m","alias":"mianyang",
     * "name":"绵阳"}],"index":"M"},{"citySet":[{"cityid":669,"firstLetter":"n","alias":"nc",
     * "name":"南昌"},{"cityid":172,"firstLetter":"n","alias":"nj","name":"南京"}],"index":"N"},
     * {"citySet":[{"cityid":122,"firstLetter":"q","alias":"qd","name":"青岛"}],"index":"Q"},
     * {"citySet":[{"cityid":241,"firstLetter":"s","alias":"sjz","name":"石家庄"},{"cityid":188,
     * "firstLetter":"s","alias":"sy","name":"沈阳"},{"cityid":5,"firstLetter":"s","alias":"su",
     * "name":"苏州"},{"cityid":4,"firstLetter":"s","alias":"sz","name":"深圳"},{"cityid":2,
     * "firstLetter":"s","alias":"sh","name":"上海"}],"index":"S"},{"citySet":[{"cityid":740,
     * "firstLetter":"t","alias":"ty","name":"太原"},{"cityid":276,"firstLetter":"t","alias":"ts",
     * "name":"唐山"},{"cityid":18,"firstLetter":"t","alias":"tj","name":"天津"}],"index":"T"},
     * {"citySet":[{"cityid":158,"firstLetter":"w","alias":"wh","name":"武汉"},{"cityid":93,
     * "firstLetter":"w","alias":"wx","name":"无锡"}],"index":"W"},{"citySet":[{"cityid":606,
     * "firstLetter":"x","alias":"xm","name":"厦门"},{"cityid":483,"firstLetter":"x","alias":"xa",
     * "name":"西安"},{"cityid":471,"firstLetter":"x","alias":"xz","name":"徐州"}],"index":"X"},
     * {"citySet":[{"cityid":342,"firstLetter":"z","alias":"zz","name":"郑州"}],"index":"Z"}]
     * token : null
     */

    private Object token;
    private List<OpenCityEntity> openCity;

    public void setToken(Object token) {
        this.token = token;
    }

    public void setOpenCity(List<OpenCityEntity> openCity) {
        this.openCity = openCity;
    }

    public Object getToken() {
        return token;
    }

    public List<OpenCityEntity> getOpenCity() {
        return openCity;
    }

    public static class OpenCityEntity {
        /**
         * citySet : [{"cityid":1,"firstLetter":"b","alias":"bj","name":"北京"}]
         * index : B
         */

        private String index;
        private List<CitySetEntity> citySet;

        public void setIndex(String index) {
            this.index = index;
        }

        public void setCitySet(List<CitySetEntity> citySet) {
            this.citySet = citySet;
        }

        public String getIndex() {
            return index;
        }

        public List<CitySetEntity> getCitySet() {
            return citySet;
        }

        public static class CitySetEntity {
            /**
             * cityid : 1
             * firstLetter : b
             * alias : bj
             * name : 北京
             */

            private int cityid;
            private String firstLetter;
            private String alias;
            private String name;

            public void setCityid(int cityid) {
                this.cityid = cityid;
            }

            public void setFirstLetter(String firstLetter) {
                this.firstLetter = firstLetter;
            }

            public void setAlias(String alias) {
                this.alias = alias;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getCityid() {
                return cityid;
            }

            public String getFirstLetter() {
                return firstLetter;
            }

            public String getAlias() {
                return alias;
            }

            public String getName() {
                return name;
            }
        }
    }
}
