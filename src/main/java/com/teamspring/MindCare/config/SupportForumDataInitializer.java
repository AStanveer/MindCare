package com.teamspring.MindCare.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.teamspring.MindCare.model.Post;
import com.teamspring.MindCare.model.Reply;
import com.teamspring.MindCare.model.Role;
import com.teamspring.MindCare.model.User; // ✅ Using Real User
import com.teamspring.MindCare.repository.PostRepository;
import com.teamspring.MindCare.repository.ReplyRepository;
import com.teamspring.MindCare.repository.UserRepository; // ✅ Using Real Repo

@Configuration
@Order(2) // Runs AFTER the main DataSeeder to ensure DB is ready
public class SupportForumDataInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReplyRepository replyRepository;
    private final PasswordEncoder passwordEncoder;

    public SupportForumDataInitializer(
            PostRepository postRepository, 
            UserRepository userRepository, 
            ReplyRepository replyRepository,
            PasswordEncoder passwordEncoder) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.replyRepository = replyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Stop if data already exists
        if (postRepository.count() > 0) {
            return;
        }

        System.out.println("🌱 Seeding Support Forum with Real Users...");

        // 1. Create Real Users (checking if they exist first)
        User alex = createUserIfNotFound("Alex M.", "alex@mindcare.com", Role.STUDENT);
        User jordan = createUserIfNotFound("Jordan P.", "jordan@mindcare.com", Role.STUDENT);
        User sam = createUserIfNotFound("Sam K.", "sam@mindcare.com", Role.STUDENT);
        User louis = createUserIfNotFound("Louis J.", "louis@mindcare.com", Role.PROFESSIONAL);
        
        // Reply Authors
        User casey = createUserIfNotFound("Casey T.", "casey@mindcare.com", Role.STUDENT);
        User morgan = createUserIfNotFound("Morgan L.", "morgan@mindcare.com", Role.STUDENT);
        User taylor = createUserIfNotFound("Taylor R.", "taylor@mindcare.com", Role.STUDENT);

        // 2. Create Post 1: Academic Stress (With Replies)
        Post p1 = new Post();
        p1.setAuthor(alex); // ✅ Sets Real User
        p1.setTitle("Managing exam stress");
        p1.setContent("Finals are coming up and I'm feeling overwhelmed. Any tips on staying calm during this time?");
        p1.setTag("Anxiety"); 
        p1.setLikesCount(12);
        p1.setCreatedAt(LocalDateTime.now().minusHours(2)); 
        
        postRepository.save(p1);

        // Replies for Post 1
        createReply(p1, casey, "I find breaking study sessions into 25-minute chunks really helpful. Take short breaks between!", 3, 60);
        createReply(p1, morgan, "Deep breathing exercises before each study session changed everything for me. Try the 4-7-8 technique!", 5, 45);
        createReply(p1, taylor, "Remember to get enough sleep! I used to pull all-nighters but found I retained more with proper rest.", 4, 30);

        // 3. Create Post 2: Self Care
        Post p2 = new Post();
        p2.setAuthor(jordan);
        p2.setTitle("Meditation helped me!");
        p2.setContent("Just wanted to share that starting a daily meditation practice has really improved my mood. Highly recommend!");
        p2.setTag("Self-Care");
        p2.setLikesCount(24);
        p2.setCreatedAt(LocalDateTime.now().minusHours(5)); 
        postRepository.save(p2);

        // 4. Create Post 3: Sleep
        Post p3 = new Post();
        p3.setAuthor(sam);
        p3.setTitle("Sleep schedule tips?");
        p3.setContent("I've been having trouble maintaining a consistent sleep schedule. What works for you?");
        p3.setTag("Sleep"); 
        p3.setLikesCount(8);
        p3.setCreatedAt(LocalDateTime.now().minusDays(1)); 
        postRepository.save(p3);

        System.out.println("✅ Forum seeded successfully with Real Users!");
    }

    // --- Helper Methods ---

    // Creates a User if they don't exist, handling password encoding and defaults
    private User createUserIfNotFound(String name, String email, Role role) {
        return userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User(name, email, passwordEncoder.encode("password"), role);
            // Fill dummy data to satisfy validation constraints
            newUser.setPhone("0000000000"); 
            newUser.setDepartment("General");
            newUser.setBio("Community member");
            return userRepository.save(newUser);
        });
    }

    private void createReply(Post post, User author, String content, int likes, int minsAgo) {
        Reply r = new Reply();
        r.setPost(post);
        r.setAuthor(author); // ✅ Expects Real User
        r.setContent(content);
        r.setLikesCount(likes);
        r.setCreatedAt(LocalDateTime.now().minusMinutes(minsAgo));
        replyRepository.save(r);
    }
}