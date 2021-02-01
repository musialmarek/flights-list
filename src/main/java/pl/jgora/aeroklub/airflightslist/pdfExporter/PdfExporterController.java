package pl.jgora.aeroklub.airflightslist.pdfExporter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import pl.jgora.aeroklub.airflightslist.model.EngineFlight;
import pl.jgora.aeroklub.airflightslist.model.PdfFlightList;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class PdfExporterController {
    //TODO extend for Glider-List and List by Pilot/User/Aircraft
    @PostMapping("/create-pdf")
    public void createPdf(@ModelAttribute(name = "pdf") PdfFlightList pdf, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=list";
        StringBuilder sb = new StringBuilder();
        List<EngineFlight> flights = pdf.getFlights();
        log.debug("flights is null {}", flights == null);
        log.debug("flights is empty {}", flights.isEmpty());
        log.debug("Flights {}", flights.size());
        response.setHeader(headerKey, headerValue);
        List<EngineFlight> list = flights.stream().map(flight -> (EngineFlight) flight).collect(Collectors.toList());
        PdfExporter exporter = new PdfExporter(list);
        exporter.export(response);
    }
}
