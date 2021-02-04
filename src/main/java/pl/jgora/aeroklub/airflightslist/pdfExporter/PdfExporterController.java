package pl.jgora.aeroklub.airflightslist.pdfExporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
public class PdfExporterController {
    @PostMapping("/create-pdf")
    public void createPdf(@ModelAttribute(name = "pdf") PdfExporter pdf, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=list";
        log.debug("pdf is null {}", pdf == null);
        response.setHeader(headerKey, headerValue);
        assert pdf != null;
        pdf.export(response);
    }
}
