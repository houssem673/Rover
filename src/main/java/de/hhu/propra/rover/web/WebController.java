package de.hhu.propra.rover.web;

import de.hhu.propra.rover.domain.Board;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import java.util.Objects;

@Controller
@SessionAttributes("board")
public class WebController {



    @ModelAttribute("board")
    public Board modelBoard(){
        return new Board();
    };

    @GetMapping("/")
    public String main(Model model) {
        return "mainpage";
    }

    @GetMapping("/game")
    public String game(Model m,@ModelAttribute("currentSteps") Integer currentSteps,@CookieValue(name = "my_highscore",defaultValue = "-1")Integer highscore){
        m.addAttribute("currentSteps",currentSteps);
        m.addAttribute("highscore", highscore);

        return "game";
    }

    @GetMapping("/won")
    public String won(@ModelAttribute("board") Board board,
                      @CookieValue(name = "my_highscore",defaultValue = "-1")Integer highscore,
                      Model m,
                      HttpServletResponse res ,
                      @ModelAttribute("currentSteps") Integer currentSteps) {
        board.reset();
        m.addAttribute("highscore", highscore);
        if(currentSteps < highscore || highscore < 0){
            saveCookie(res, "my_highscore", currentSteps);
        }
        return "won";
    }

    @PostMapping("/game/{direction}")
    public String move(@PathVariable("direction") String direction,RedirectAttributes redAtt,
                       @ModelAttribute("board") Board board , Integer currentSteps) {
        switch (direction) {
            case "up" -> board.moveUp();
            case "down" -> board.moveDown();
            case "left" -> board.moveLeft();
            case "right" -> board.moveRight();
            default -> throw new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT);
        }
        redAtt.addFlashAttribute("currentSteps",currentSteps+1);

        return board.isGameWon() ?
                "redirect:/won" :
                "redirect:/game";
    }

    @GetMapping("/reset")
    private String reset( @ModelAttribute("board") Board board, Model model){
        board.reset();
        model.addAttribute("currentSteps",0);
        return "game";
    };

    private void saveCookie(HttpServletResponse response, String name, Object value) {
        Cookie cookie = new Cookie(name, Objects.toString(value));
        cookie.setSecure(true);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


}
