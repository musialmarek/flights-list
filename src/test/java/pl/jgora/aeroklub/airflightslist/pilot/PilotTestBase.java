package pl.jgora.aeroklub.airflightslist.pilot;

import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;


public class PilotTestBase {
    static PilotBuilder builder() {
        return new PilotBuilder();
    }

    static class PilotBuilder {
        private Long id = 1L;
        private String name = "Kowalski Jan";
        private String licence = "123";
        private Boolean active = true;
        private Boolean gliderPilot = true;
        private Boolean gliderInstructor = true;
        private Boolean enginePilot = true;
        private Boolean engineInstructor = true;
        private Boolean tow = true;
        private LocalDate medicine = LocalDate.now().plusDays(14);
        private LocalDate theory = LocalDate.now().plusDays(14);
        private LocalDate gliderPractise = LocalDate.now().plusDays(14);
        private LocalDate enginePractise = LocalDate.now().plusDays(14);
        private LocalDate sepl = LocalDate.now().plusDays(14);
        private LocalDate fis = LocalDate.now().plusDays(14);
        private LocalDate fia = LocalDate.now().plusDays(14);

        PilotBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        PilotBuilder withName(String name) {
            this.name = name;
            return this;
        }

        PilotBuilder withLicence(String licence) {
            this.licence = licence;
            return this;
        }

        PilotBuilder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        PilotBuilder withGliderPilot(Boolean gliderPilot) {
            this.gliderPilot = gliderPilot;
            return this;
        }

        PilotBuilder withGliderInstructor(Boolean gliderInstructor) {
            this.gliderInstructor = gliderInstructor;
            return this;
        }

        PilotBuilder withEnginePilot(Boolean enginePilot) {
            this.enginePilot = enginePilot;
            return this;
        }

        PilotBuilder withEngineInstructor(Boolean engineInstructor) {
            this.engineInstructor = engineInstructor;
            return this;
        }

        PilotBuilder withTow(Boolean tow) {
            this.tow = tow;
            return this;
        }

        PilotBuilder withMedicine(LocalDate medicine) {
            this.medicine = medicine;
            return this;
        }

        PilotBuilder withTheory(LocalDate theory) {
            this.theory = theory;
            return this;
        }

        PilotBuilder withGliderPractise(LocalDate gliderPractise) {
            this.gliderPractise = gliderPractise;
            return this;
        }

        PilotBuilder withEnginePractise(LocalDate enginePractise) {
            this.enginePractise = enginePractise;
            return this;
        }

        PilotBuilder withSepl(LocalDate sepl) {
            this.sepl = sepl;
            return this;
        }

        PilotBuilder withFis(LocalDate fis) {
            this.fis = fis;
            return this;
        }

        PilotBuilder withFia(LocalDate fia) {
            this.fia = fia;
            return this;
        }

        Pilot build() {
            return new Pilot(id, name, licence, active, gliderPilot, gliderInstructor, enginePilot, engineInstructor, tow, medicine, theory, gliderPractise, enginePractise, sepl, fis, fia);
        }


    }
}
