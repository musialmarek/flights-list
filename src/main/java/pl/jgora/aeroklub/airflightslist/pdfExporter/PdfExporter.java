package pl.jgora.aeroklub.airflightslist.pdfExporter;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;
import pl.jgora.aeroklub.airflightslist.model.StartMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class PdfExporter {

    private List<EngineFlight> engineFlights;
    private List<GliderFlight> gliderFlights;
    private ListType type;
    private boolean glider;

    public PdfExporter(List<EngineFlight> engineFlights, ListType type) {
        this.engineFlights = engineFlights;
        this.type = type;
        this.glider = false;
    }

    public PdfExporter(ListType type, List<GliderFlight> gliderFlights) {
        this.gliderFlights = gliderFlights;
        this.type = type;
        this.glider = true;
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
        document.close();
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
        List<AbstractFlight> flights = getAbstractFlights();
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
            table.addCell(new Phrase(getString(flights.get(i).getFlightTime()), font));
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
                    table.addCell(new Phrase(getString(tow.getFlightTime()), font));
                } else {
                    for (int j = 0; j < 4; j++) {
                        table.addCell(new Phrase("-", font));
                    }
                }
            }
        }
    }

    private List<AbstractFlight> getAbstractFlights() {
        List<AbstractFlight> flights = new ArrayList<>();
        if (this.glider) {
            flights = gliderFlights.stream().map(flight -> (AbstractFlight) flight).collect(Collectors.toList());
        } else {
            flights = engineFlights.stream().map(flight -> (AbstractFlight) flight).collect(Collectors.toList());
        }
        return flights;
    }

    private String getString(Integer flightTime) {
        Integer hours = flightTime / 60;
        Integer minutes = flightTime % 60;
        return String.format("%01d", hours) + ":" + String.format("%02d", minutes);
    }

    private String getTableHeader() {
        StringBuilder sb = new StringBuilder();
        List<AbstractFlight> flights = getAbstractFlights();
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