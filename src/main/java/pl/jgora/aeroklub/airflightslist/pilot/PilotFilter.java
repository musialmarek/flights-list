package pl.jgora.aeroklub.airflightslist.pilot;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Slf4j
public class PilotFilter {
    private String name;
    private String licence;
    private Boolean active;
    private Boolean gliderPilot;
    private Boolean gliderInstructor;
    private Boolean enginePilot;
    private Boolean engineInstructor;
    private Boolean tow;
    private Map<String, String> filters;
    private String whereSection;


    public Map<String, String> getFilters() {
        invoke();
        return filters;
    }

    public String getWhereSection() {
        invoke();
        return whereSection;
    }

    private void invoke() {
        StringBuilder whereSectionBuilder = new StringBuilder();
        filters = new HashMap<>();
        if (name != null && !name.isEmpty()) {
            whereSectionBuilder.append(" p.name like concat('%',:name,'%') AND");
            filters.put("name", name);
        }
        if (licence != null && !licence.isEmpty()) {
            whereSectionBuilder.append(" p.licence like concat('%',:licence,'%') AND");
            filters.put("licence", licence);
        }
        if (active != null) {
            whereSectionBuilder.append(" p.active=").append(active).append(" AND");
        }
        if (gliderPilot != null) {
            whereSectionBuilder.append(" p.gliderPilot=").append(gliderPilot).append(" AND");
        }
        if (gliderInstructor != null) {
            whereSectionBuilder.append(" p.gliderInstructor=").append(gliderInstructor).append(" AND");
        }
        if (enginePilot != null) {
            whereSectionBuilder.append(" p.enginePilot=").append(enginePilot).append(" AND");
        }
        if (engineInstructor != null) {
            whereSectionBuilder.append(" p.engineInstructor=").append(engineInstructor).append(" AND");
        }
        if (tow != null) {
            whereSectionBuilder.append(" p.tow=").append(tow).append(" AND");
        }
        whereSectionBuilder.append(" p.id IS NOT NULL ");
        whereSection = whereSectionBuilder.toString();
        log.debug("WHERE SECTION \"{}\"", whereSection);
    }
}
