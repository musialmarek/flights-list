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
    //TODO extend for Glider-List and List by Pilot/User/Aircraft
    @PostMapping("/create-pdf")
    public void createPdf(@ModelAttribute(name = "pdf") PdfExporter pdf, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=list";
        log.debug("pdf is null {}", pdf == null);
        assert pdf != null;
        List<EngineFlight> flights = pdf.getEngineFlights();
        log.debug("flights is null {}", flights == null);
        response.setHeader(headerKey, headerValue);
        pdf.export(response);
    }
}
