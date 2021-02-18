package pl.jgora.aeroklub.airflightslist.pilot;

import pl.jgora.aeroklub.airflightslist.model.Pilot;

import java.time.LocalDate;


public class PilotTestBase {
    public static PilotBuilder builder() {
        return new PilotBuilder();
    }

    public static class PilotBuilder {
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
        private Boolean nativeMember;

        public PilotBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public PilotBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public PilotBuilder withLicence(String licence) {
            this.licence = licence;
            return this;
        }

        public PilotBuilder withActive(Boolean active) {
            this.active = active;
            return this;
        }

        public PilotBuilder withGliderPilot(Boolean gliderPilot) {
            this.gliderPilot = gliderPilot;
            return this;
        }

        public PilotBuilder withGliderInstructor(Boolean gliderInstructor) {
            this.gliderInstructor = gliderInstructor;
            return this;
        }

        public PilotBuilder withEnginePilot(Boolean enginePilot) {
            this.enginePilot = enginePilot;
            return this;
        }

        public PilotBuilder withEngineInstructor(Boolean engineInstructor) {
            this.engineInstructor = engineInstructor;
            return this;
        }

        public PilotBuilder withTow(Boolean tow) {
            this.tow = tow;
            return this;
        }

        public PilotBuilder withMedicine(LocalDate medicine) {
            this.medicine = medicine;
            return this;
        }

        public PilotBuilder withTheory(LocalDate theory) {
            this.theory = theory;
            return this;
        }

        public PilotBuilder withGliderPractise(LocalDate gliderPractise) {
            this.gliderPractise = gliderPractise;
            return this;
        }

        public PilotBuilder withEnginePractise(LocalDate enginePractise) {
            this.enginePractise = enginePractise;
            return this;
        }

        public PilotBuilder withSepl(LocalDate sepl) {
            this.sepl = sepl;
            return this;
        }

        public PilotBuilder withFis(LocalDate fis) {
            this.fis = fis;
            return this;
        }

        public PilotBuilder withFia(LocalDate fia) {
            this.fia = fia;
            return this;
        }

        public PilotBuilder withNativeMember(Boolean nativeMember) {
            this.nativeMember = nativeMember;
            return this;
        }

        public Pilot build() {
            return new Pilot(id, name, "", licence, active, gliderPilot, gliderInstructor, enginePilot, engineInstructor, tow, medicine, theory, gliderPractise, enginePractise, sepl, fis, fia, nativeMember);
        }
    }
}
