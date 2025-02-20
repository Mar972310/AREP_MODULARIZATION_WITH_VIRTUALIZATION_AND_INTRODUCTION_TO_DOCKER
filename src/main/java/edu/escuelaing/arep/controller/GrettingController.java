
package edu.escuelaing.arep.controller;



import edu.escuelaing.arep.annotation.GetMapping;
import edu.escuelaing.arep.annotation.RequestParam;
import edu.escuelaing.arep.annotation.RestController;


@RestController
public class GrettingController {

    @GetMapping("/greeting")
    public String greeting(@RequestParam(value="name",defaultValue="world") String name) {
        return "Hello " + name + " !";
    }
}
