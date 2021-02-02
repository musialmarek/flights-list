package pl.jgora.aeroklub.airflightslist.pdfExporter;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.jgora.aeroklub.airflightslist.model.AbstractFlight;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.GliderFlight;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
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

    private void writeTableHeader(PdfPTable table) {
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
        table.setWidths(widthsArray);
        PdfPCell cell = new PdfPCell();
        for (String header : headers) {
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table) {
        List<AbstractFlight> flights = getAbstractFlights();
        for (int i = 0; i < flights.size(); i++) {
            //Lp.
            table.addCell(String.valueOf(i + 1));
            if (!ListType.DAILY.equals(type)) {
                table.addCell(flights.get(i).getDate().toString());
            }
            if (!ListType.USER.equals(type)) {
                table.addCell(flights.get(i).getPicName());
                table.addCell(flights.get(i).getCopilotName());
            }
            if (glider) {
                table.addCell(gliderFlights.get(i).getStartMethod().toString());
            }
            table.addCell(flights.get(i).getTask());
            table.addCell(flights.get(i).getAircraftType());
            table.addCell(flights.get(i).getAircraftRegistrationNumber());
            table.addCell(flights.get(i).getStart().toString());
            table.addCell(flights.get(i).getTouchdown().toString());
            table.addCell(getString(flights.get(i).getFlightTime()));
            if (glider) {
                EngineFlight tow = gliderFlights.get(i).getEngineFlight();
                StringBuilder sb = new StringBuilder();
                sb.append(tow.getPicName());
                if (tow.getCopilot() != null) {
                    sb.append(" ").append(tow.getCopilotName());
                }
                table.addCell(sb.toString());
                sb.delete(0, sb.length());
                table.addCell(sb.
                        append(tow.getAircraftType())
                        .append(" ")
                        .append(tow.getAircraftRegistrationNumber())
                        .toString());
                table.addCell(tow.getTouchdown().toString());
                table.addCell(getString(tow.getFlightTime()));
            }
        }
    }

    private List<AbstractFlight> getAbstractFlights() {
        List<AbstractFlight> flights = new ArrayList<>();
        if (glider) {
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

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph(getTableHeader()));
        document.add(new Paragraph(""));
        PdfPTable table = new PdfPTable(9);
        table.setWidthPercentage(100);
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
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
//TODO extend for Glider-List and List by Pilot/User/Aircraft