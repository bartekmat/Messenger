package com.gruzini.messenger.models;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LogoService {

    private static Map<String, String> logos = new HashMap<>();

    static {
        logos.put("Dog", "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcQy0gcXavtE10AMBb1o_J_fIR0npuwuwVgJHg&usqp=CAU");
        logos.put("Cat", "https://cdn0.iconfinder.com/data/icons/black-cat-emoticon-filled/64/cute_cat_kitten_face_per_avatar-03-512.png");
        logos.put("Mouse", "https://previews.123rf.com/images/vslp/vslp1205/vslp120500005/13628969-cute-mouse-web-user-avatar-or-icon.jpg");
        logos.put("Dolphin", "https://image.freepik.com/free-vector/illustration-cute-dolphin-avatar_79416-106.jpg");
        logos.put("Fox", "https://img.favpng.com/19/7/15/koala-cartoon-fox-avatar-png-favpng-v7ir0MbUDz8DRDmVVX4Zz1EEU.jpg");
        logos.put("Elephant", "https://i.pinimg.com/originals/a1/d7/88/a1d7886d6914cba09134d623bff28e11.png");
        logos.put("Snail","https://i.pinimg.com/originals/a1/d7/88/a1d7886d6914cba09134d623bff28e11.png" );
        logos.put("Snake","https://st.depositphotos.com/1066172/1365/v/450/depositphotos_13657140-stock-illustration-nice-green-snake.jpg" );
        logos.put("Parrot","https://image.shutterstock.com/image-vector/wonderful-multi-color-parrot-bird-260nw-1169750224.jpg" );
        logos.put("Bear", "https://www.crushpixel.com/static14/preview2/cute-teddy-bear-holding-honey-1495885.jpg");
        logos.put("Rabbit", "https://w0.pngwave.com/png/679/657/rabbit-cartoon-elephant-rabbit-png-clip-art.png");
        logos.put("Eel", "https://image.shutterstock.com/image-vector/cartoon-cute-eel-260nw-488380525.jpg");
        logos.put("Fish", "https://as2.ftcdn.net/jpg/01/73/60/69/1000_F_173606977_WI9CdsvldESpEuyoeRSfuq7SaiBOFZFc.jpg");
        logos.put("Elk", "https://i.pinimg.com/originals/f1/73/8f/f1738f87e483311f755470cb181b28aa.jpg");
    }

    public String getLogoLink(final String principalName){
        if(logos.containsKey(principalName)){
            return logos.get(principalName);
        }else {
            return "https://cdn2.iconfinder.com/data/icons/social-flat-buttons-3/512/anonymous-512.png";
        }

    }
}
