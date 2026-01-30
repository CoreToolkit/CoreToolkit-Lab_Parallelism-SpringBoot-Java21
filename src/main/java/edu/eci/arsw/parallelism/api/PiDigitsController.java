package edu.eci.arsw.parallelism.api;

import edu.eci.arsw.parallelism.core.PiDigitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pi")
@Tag(name = "Pi API", description = "API for calculating hexadecimal digits of π")
public class PiDigitsController {

    private final PiDigitsService service;

    public PiDigitsController(PiDigitsService service) {
        this.service = service;
    }

    @Operation(
            summary = "Calculate hexadecimal digits of Pi",
            description = """
            Calculates hexadecimal digits of π (Pi) using the BBP algorithm.
            
            **Example:**
            - `start=0, count=5` → Returns first 5 hexadecimal digits after the point
            - Digits are returned in hexadecimal format (0-9, A-F)
            
            **Constraints:**
            - `start` must be ≥ 0
            - `count` must be ≥ 1
            
            **Implementation:** This version uses sequential calculation.
            """
    )
    @GetMapping("/digits")
    public PiResponse digits(
            @Parameter(
                    description = "Starting position (0-based) after the hexadecimal point",
                    example = "0",
                    required = true
            )
            @RequestParam int start,

            @Parameter(
                    description = "Number of hexadecimal digits to calculate",
                    example = "10",
                    required = true
            )
            @RequestParam int count,

            @RequestParam(required = false) Integer threads,

            @RequestParam(required = false) String strategy) {

        String digits = service.calculate(start, count, threads, strategy);
        return new PiResponse(start, count, digits);
    }
}