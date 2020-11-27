package pl.jgora.aeroklub.airflightslist.aircraft;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AircraftFilter {
    private String type;
    private String registration;
    private Boolean active;
    private Boolean engine;
    private String whereSection;
    private Map<String, String> filters;

    public String getWhereSection() {
        invoke();
        return whereSection;
    }

    public Map<String, String> getFilters() {
        invoke();
        return filters;
    }

    private void invoke() {
        StringBuilder whereSectionBuilder = new StringBuilder();
        filters = new HashMap<>();
        if (type != null && !type.isEmpty()) {
            whereSectionBuilder.append(" a.type like concat('%',:type,'%') AND");
            filters.put("type", type);
        }
        if (registration != null && !registration.isEmpty()) {
            whereSectionBuilder.append(" a.registrationNumber like concat('%',:registration,'%') AND");
            filters.put("registration", registration);
        }
        if (active != null) {
            whereSectionBuilder.append(" a.active=").append(active).append(" AND");
        }
        if (engine != null) {
            whereSectionBuilder.append(" a.engine=").append(engine).append(" AND");
        }
        whereSectionBuilder.append(" a.id IS NOT NULL ");
        whereSection = whereSectionBuilder.toString();
    }
}
