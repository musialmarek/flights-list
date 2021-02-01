package pl.jgora.aeroklub.airflightslist.pdfExporter;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
public class PdfExporter {
    private List<EngineFlight> list;
    private static List<String> engineColumns = List.of("Lp.",
            "PILOT/UCZEŃ PILOT",
            "II PILOT/INSTRUKTOR",
            "ZADANIE",
            "TYP ST. POW.",
            "ZNAKI",
            "START",
            "LĄDOWANIE",
            "CZAS LOTU");

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        for (String header : engineColumns) {
            cell.setPhrase(new Phrase(header));
            table.addCell(cell);
        }
    }

    private void writeTableData(PdfPTable table) {
        for (int i = 0; i < list.size(); i++) {
            table.addCell(String.valueOf(i + 1));
            table.addCell(list.get(i).getPicName());
            table.addCell(list.get(i).getCopilotName());
            table.addCell(list.get(i).getTask());
            table.addCell(list.get(i).getAircraftType());
            table.addCell(list.get(i).getAircraftRegistrationNumber());
            table.addCell(list.get(i).getStart().toString());
            table.addCell(list.get(i).getTouchdown().toString());
            Integer hours = list.get(i).getFlightTime() / 60;
            Integer minutes = list.get(i).getFlightTime() % 60;
            String time = String.format("%01d", hours) + ":" + String.format("%02d", minutes);
            table.addCell(time);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();
        document.add(new Paragraph("Hello World"));
        PdfPTable table = new PdfPTable(9);
        table.setWidths(new float[]{0.6f, 3.0f, 3.0f, 1.5f, 1.5f, 1.5f, 1.0f, 1.0f, 1.0f});
        table.setWidthPercentage(100);
        writeTableHeader(table);
        writeTableData(table);
        document.add(table);
        document.close();
    }
}
//TODO extend for Glider-List and List by Pilot/User/Aircraft