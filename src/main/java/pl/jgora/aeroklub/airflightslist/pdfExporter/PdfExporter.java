package pl.jgora.aeroklub.airflightslist.pdfExporter;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jgora.aeroklub.airflightslist.abstractFlight.AbstractFlightService;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class PdfExporter {

    private List<EngineFlight> engineFlights = new ArrayList<>();
    private List<GliderFlight> gliderFlights = new ArrayList<>();
    private List<AbstractFlight> flights = new ArrayList<>();
    private List<AbstractFlight> towFlights = new ArrayList<>();
    private ListType type;
    private boolean glider;

    public PdfExporter(List<EngineFlight> engineFlights, ListType type) {
        this.engineFlights = engineFlights;
        this.type = type;
        this.glider = false;
        flights = AbstractFlightService.engineToAbstractFlightList(engineFlights);
    }

    public PdfExporter(ListType type, List<GliderFlight> gliderFlights) {
        this.gliderFlights = gliderFlights;
        this.type = type;
        this.glider = true;
        flights = AbstractFlightService.gliderToAbstractFlightList(gliderFlights);
        towFlights = gliderFlights
                .stream()
                .filter(flight -> StartMethod.ATTO.equals(flight.getStartMethod()))
                .map(GliderFlight::getEngineFlight)
                .map(engineFlight -> (AbstractFlight) engineFlight)
                .collect(Collectors.toList());
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph(getTableHeader()));
        document.add(new Paragraph("\n"));
        PdfPTable table = writeTableHeader();
        table.setWidthPercentage(100);
        writeTableData(table);
        document.add(table);
        writeSummary(document);
        document.close();
    }

    private void writeSummary(Document document) {
        document.add(new Paragraph("Podsumowanie"));
        document.add(new Paragraph("\n"));
        PdfPTable table = new PdfPTable(3);
        PdfPCell cell = new PdfPCell();
        cell.setPhrase(new Phrase(" "));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Liczba lotów"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Czas lotu"));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Razem"));
        table.addCell(cell);
        int flightTime = getFlightTime(flights);
        log.debug("main flights time: {}", flightTime);
        int towingTime = getFlightTime(towFlights);
        log.debug("towing time {}", towingTime);
        int flightsQuantity = flights.size();
        int minutes = flightTime;
        if (ListType.DAILY.equals(type)) {
            minutes = flightTime + towingTime;
            flightsQuantity = flights.size() + towFlights.size();
        }
        String numberOfFlights = Integer.toString(flightsQuantity);
        table.addCell(numberOfFlights);
        log.debug("summary minutes {}", minutes);
        String allTime = getTimeFormatFromInteger(minutes);
        table.addCell(allTime);
        if (glider && ListType.DAILY.equals(type)) {
            table.addCell("SZYBOWCE");
            table.addCell(Integer.toString(gliderFlights.size()));
            table.addCell(getTimeFormatFromInteger(flightTime));
            table.addCell("SAMOLOTY");
            table.addCell(Integer.toString(towFlights.size()));
            table.addCell(getTimeFormatFromInteger(towingTime));
        }
        if (!ListType.AIRCRAFT.equals(type)) {
            Set<String> aircrafts = getAircrafts(flights);
            Set<String> towingAircrafts = getAircrafts(towFlights);
            setSummaryToTableByAircrafts(table, aircrafts, this.flights);
            setSummaryToTableByAircrafts(table, towingAircrafts, this.towFlights);
        }
        document.add(table);
    }

    private void setSummaryToTableByAircrafts(PdfPTable table, Set<String> aircrafts, List<AbstractFlight> flights) {
        for (String aircraftRegistrationNumber : aircrafts) {
            List<AbstractFlight> aircraftsFlights = flights.stream()
                    .filter(flight -> aircraftRegistrationNumber.equals(flight.getAircraftRegistrationNumber()))
                    .collect(Collectors.toList());
            String aircraft = getAircraft(aircraftsFlights.get(0));
            table.addCell(aircraft);
            String aircraftsFlightsQuantity = Integer.toString(aircraftsFlights.size());
            table.addCell(aircraftsFlightsQuantity);
            String aircraftsFlightTime = getTimeFormatFromInteger(getFlightTime(aircraftsFlights));
            table.addCell(aircraftsFlightTime);
        }
    }

    private String getAircraft(AbstractFlight flight) {
        return flight.getAircraftType() + " " + flight.getAircraftRegistrationNumber();
    }

    private Set<String> getAircrafts(List<AbstractFlight> flights) {
        return flights.stream().map(AbstractFlight::getAircraftRegistrationNumber).collect(Collectors.toSet());
    }

    private Integer getFlightTime(List<AbstractFlight> flights) {
        return flights
                .stream()
                .map(AbstractFlight::getFlightTime)
                .reduce(Integer::sum).orElse(0);
    }

    private PdfPTable writeTableHeader() {
        List<String> headers = new ArrayList<>();
        List<Float> widths = new ArrayList<>();
        headers.add("Lp.");
        widths.add(0.6f);
        if (!ListType.DAILY.equals(this.type)) {
            headers.add("DATA");
            widths.add(2.0f);
        }
        if (!ListType.USER.equals(this.type)) {
            headers.add("PILOT/UCZEŃ");
            widths.add(3.0f);
            headers.add("II PILOT/INSTRUKTOR");
            widths.add(3.0f);
        }
        if (glider) {
            headers.add("RODZ. ST.");
            widths.add(0.8f);
        }
        headers.add("ZAD");
        widths.add(1.5f);
        if (!ListType.AIRCRAFT.equals(type)) {
            headers.add("TYP");
            widths.add(1.5f);
            headers.add("ZNAKI");
            widths.add(1.5f);
        }
        headers.add("START");
        widths.add(1.0f);
        headers.add("LĄD");
        widths.add(1.0f);
        headers.add("CZAS");
        widths.add(1.0f);
        if (glider) {
            headers.add("HOLOWNIK");
            widths.add(3.0f);
            headers.add("HOLÓWKA");
            widths.add(3.0f);
            headers.add("LĄD.");
            widths.add(1.0f);
            headers.add("CZAS");
            widths.add(1.0f);
        }
        float[] widthsArray = new float[widths.size()];
        for (int i = 0; i < widthsArray.length; i++) {
            widthsArray[i] = widths.get(i);
        }
        PdfPTable table = new PdfPTable(headers.size());
        table.setWidths(widthsArray);
        log.debug("headers = widths {}", headers.size() == widthsArray.length);
        PdfPCell cell = new PdfPCell();
        for (String header : headers) {
            cell.setPhrase(new Phrase(header, new Font(1, 8.0f)));
            table.addCell(cell);
        }
        return table;
    }

    private void writeTableData(PdfPTable table) {
        for (int i = 0; i < flights.size(); i++) {
            //Lp.
            int fontFamily = 1;
            float fontSize = 8.0f;
            Font font = new Font(fontFamily, fontSize);
            table.addCell(new Phrase(String.valueOf(i + fontFamily), font));
            if (!ListType.DAILY.equals(type)) {
                table.addCell(new Phrase(flights.get(i).getDate().toString(), font));
            }
            if (!ListType.USER.equals(type)) {
                table.addCell(new Phrase(flights.get(i).getPicName(), font));
                table.addCell(new Phrase(flights.get(i).getCopilotName(), font));
            }
            if (glider) {
                table.addCell(new Phrase(gliderFlights.get(i).getStartMethod().toString(), font));
            }
            table.addCell(new Phrase(flights.get(i).getTask(), font));
            table.addCell(new Phrase(flights.get(i).getAircraftType(), font));
            table.addCell(new Phrase(flights.get(i).getAircraftRegistrationNumber(), font));
            table.addCell(new Phrase(flights.get(i).getStart().toString(), font));
            table.addCell(new Phrase(flights.get(i).getTouchdown().toString(), font));
            table.addCell(new Phrase(getTimeFormatFromInteger(flights.get(i).getFlightTime()), font));
            if (glider) {
                if (StartMethod.ATTO.equals(gliderFlights.get(i).getStartMethod())) {
                    EngineFlight tow = gliderFlights.get(i).getEngineFlight();
                    StringBuilder sb = new StringBuilder();
                    sb.append(tow.getPicName());
                    if (tow.getCopilot() != null) {
                        sb.append(" ").append(tow.getCopilotName());
                    }
                    table.addCell(new Phrase(sb.toString(), font));
                    sb.delete(0, sb.length());
                    table.addCell(new Phrase(sb.
                            append(tow.getAircraftType())
                            .append(" ")
                            .append(tow.getAircraftRegistrationNumber())
                            .toString(), font));
                    table.addCell(new Phrase(tow.getTouchdown().toString(), font));
                    table.addCell(new Phrase(getTimeFormatFromInteger(tow.getFlightTime()), font));
                } else {
                    for (int j = 0; j < 4; j++) {
                        table.addCell(new Phrase("-", font));
                    }
                }
            }
        }
    }

    private String getTimeFormatFromInteger(Integer flightTime) {
        Integer hours = flightTime / 60;
        Integer minutes = flightTime % 60;
        return String.format("%01d", hours) + ":" + String.format("%02d", minutes);
    }

    private String getTableHeader() {
        StringBuilder sb = new StringBuilder();
        if (ListType.DAILY.equals(type)) {
            if (glider) {
                sb.append("LISTA SZYBOWCOWA");
            } else {
                sb.append("LISTA SAMOLOTOWA");
            }
            sb.append(" ").append(flights.get(0).getDate().toString());
        } else if (ListType.AIRCRAFT.equals(type)) {
            sb.append(flights.get(0).getAircraftType()).append(" ").append(flights.get(0).getAircraftRegistrationNumber());
        } else {
            sb.append("LOTY PILOTA");
        }
        return sb.toString();
    }

    public enum ListType {DAILY, AIRCRAFT, PILOT, USER}
}