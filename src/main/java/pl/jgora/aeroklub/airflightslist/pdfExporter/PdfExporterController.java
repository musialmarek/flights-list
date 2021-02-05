package pl.jgora.aeroklub.airflightslist.pdfExporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@Slf4j
public class PdfExporterController {
    @PostMapping("/create-pdf")
    public void createPdf(@ModelAttribute(name = "pdf") PdfExporter pdf, HttpServletResponse response) throws IOException {
        log.debug("pdf is null {}", pdf == null);
        assert pdf != null;
        if (pdf.isGlider()) {
            pdf = new PdfExporter(pdf.getType(), pdf.getGliderFlights());
        } else {
            pdf = new PdfExporter(pdf.getEngineFlights(), pdf.getType());
        }
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=list";
        response.setHeader(headerKey, headerValue);
        pdf.export(response);
    }
}
