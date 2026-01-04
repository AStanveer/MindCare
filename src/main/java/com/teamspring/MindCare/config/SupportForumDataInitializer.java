package com.teamspring.MindCare.config;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.teamspring.MindCare.model.Post;
import com.teamspring.MindCare.model.Reply;
import com.teamspring.MindCare.model.Role;
import com.teamspring.MindCare.model.UserTemp;
import com.teamspring.MindCare.repository.PostRepository;
import com.teamspring.MindCare.repository.ReplyRepository;
import com.teamspring.MindCare.repository.UserTempRepository;

@Component
public class SupportForumDataInitializer implements CommandLineRunner {

    private final PostRepository postRepository;
    private final UserTempRepository tempUserRepository;
    private final ReplyRepository replyRepository;

    public SupportForumDataInitializer(PostRepository postRepository, UserTempRepository tempUserRepository, ReplyRepository replyRepository) {
        this.postRepository = postRepository;
        this.tempUserRepository = tempUserRepository;
        this.replyRepository = replyRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        
        // Stop if data already exists
        if (postRepository.count() > 0) {
            return;
        }

        System.out.println("Seeding database with sample data...");

        // 1. Create Users
        UserTemp alex = new UserTemp("Alex M.", "alex@example.com", "password", Role.STUDENT);
        UserTemp jordan = new UserTemp("Jordan P.", "jordan@example.com", "password", Role.STUDENT);
        UserTemp sam = new UserTemp("Sam K.", "sam@example.com", "password", Role.STUDENT);
        UserTemp proffesional = new UserTemp("Lous J.", "louis@example.com", "password", Role.PROFESSIONAL);
        
        // Reply Authors
        UserTemp casey = new UserTemp("Casey T.", "casey@example.com", "password", Role.STUDENT);
        UserTemp morgan = new UserTemp("Morgan L.", "morgan@example.com", "password", Role.STUDENT);
        UserTemp taylor = new UserTemp("Taylor R.", "taylor@example.com", "password", Role.STUDENT);

        tempUserRepository.saveAll(Arrays.asList(alex, jordan, sam, casey, morgan, taylor, proffesional));

        // 2. Create Post 1: Academic Stress (With Replies)
        Post p1 = new Post();
        p1.setAuthor(alex);
        p1.setTitle("Managing exam stress");
        p1.setContent("Finals are coming up and I'm feeling overwhelmed. Any tips on staying calm during this time?");
        p1.setTag("Academic Stress"); // Match your CSS logic
        p1.setLikesCount(12);
        p1.setCreatedAt(LocalDateTime.now().minusHours(2)); // "2 hours ago"
        
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
        p2.setCreatedAt(LocalDateTime.now().minusHours(5)); // "5 hours ago"
        postRepository.save(p2);

        // 4. Create Post 3: Sleep
        Post p3 = new Post();
        p3.setAuthor(sam);
        p3.setTitle("Sleep schedule tips?");
        p3.setContent("I've been having trouble maintaining a consistent sleep schedule. What works for you?");
        p3.setTag("Sleep"); // CSS class logic handles this
        p3.setLikesCount(8);
        p3.setCreatedAt(LocalDateTime.now().minusDays(1)); // "1 day ago"
        postRepository.save(p3);

        System.out.println("Database seeded successfully!");
    }

    // Helper to create replies cleanly
    private void createReply(Post post, UserTemp author, String content, int likes, int minsAgo) {
        Reply r = new Reply();
        r.setPost(post);
        r.setAuthor(author);
        r.setContent(content);
        r.setLikesCount(likes);
        r.setCreatedAt(LocalDateTime.now().minusMinutes(minsAgo));
        replyRepository.save(r);
    }
}