package com.example.kongsight.test;

import android.content.Context;
import android.util.Log;

import com.example.kongsight.database.ContentEntity;
import com.example.kongsight.database.UserEntity;
import com.example.kongsight.model.AppRepositorySync;

import java.util.List;

public class TestDB {

    private static final String TAG = "DbTest"; // Logcat 中搜索 DbTest 查询相应日志

    public static void testDB(Context context) {
        AppRepositorySync repo = new AppRepositorySync(context); // 调用 AppRepositorySync 的方法

        try {
            // delete database
            boolean deleted = context.deleteDatabase("tour_app_database");
            if (deleted) {
                Log.d(TAG, "Database deleted successfully");
            } else {
                Log.e(TAG, "Failed to delete database");
            }
            // Test User Operations

            // 1. Register a new user
            Long userId = repo.registerUser("testuser", "password123");
            if (userId != null) {
                Log.d(TAG, "User registered with ID: " + userId);
            } else {
                Log.d(TAG, "User registration failed: username already exists");
                // Attempt to login to get existing user ID
                userId = repo.loginUser("testuser", "password123");
                if (userId != null) {
                    Log.d(TAG, "Existing user logged in with ID: " + userId);
                } else {
                    throw new RuntimeException("Failed to register or login user");
                }
            }

            // 2. Login the user
            Long loggedInId = repo.loginUser("testuser", "password123");
            if (loggedInId != null) {
                Log.d(TAG, "User logged in with ID: " + loggedInId);
            } else {
                Log.d(TAG, "Login failed");
            }

            // 3. Update user profile
            repo.updateUserProfile(userId, "This is a test bio", "test@email.com", "newpassword123");
            Log.d(TAG, "User profile updated");

            // 4. Get user admin status
            boolean isAdmin = repo.getUserAdminStatus(userId);
            Log.d(TAG, "User is admin: " + isAdmin);

            // 5. Get user by ID
            UserEntity user = repo.getUserById(userId);
            if (user != null) {
                Log.d(TAG, "Retrieved user: " + user.getUsername());
            }

            // 6. Get user by username
            UserEntity userByName = repo.getUserByUsername("testuser");
            if (userByName != null) {
                Log.d(TAG, "Retrieved user by name: " + userByName.getUsername());
            }

            // 7. Get all users
            List<UserEntity> allUsers = repo.getAllUsers();
            Log.d(TAG, "Total users: " + allUsers.size());

            /* Test Content Operations */

            // 8. Create new content
            repo.createContent("Test Title", "http://example.com/image.jpg", "This is a test description", 12.34, 56.78, userId);
            Log.d(TAG, "Content created");

            // 9. Get all contents
            List<ContentEntity> allContents = repo.getAllContents();
            if (!allContents.isEmpty()) {
                Log.d(TAG, "Total contents: " + allContents.size());
                ContentEntity firstContent = allContents.get(0);
                long contentId = firstContent.getId();

                // 10. Get content by ID
                ContentEntity content = repo.getContentById(contentId);
                if (content != null) {
                    Log.d(TAG, "Retrieved content title: " + content.getTitle());
                }

                // 11. Edit content
                repo.editContent(contentId, "Updated Title", null, "Updated description", null, null);
                Log.d(TAG, "Content edited");

                // 12. Delete content by ID
                repo.deleteContentByID(contentId);
                Log.d(TAG, "Content deleted");
            } else {
                Log.d(TAG, "No contents found");
            }
            repo.createContent(
                    "Eiffel Tower",
                    "https://example.com/eiffel_tower.jpg",
                    "The Eiffel Tower is a wrought-iron lattice tower on the Champ de Mars in Paris, France. It is named after the engineer Gustave Eiffel, whose company designed and built the tower.",
                    2.294481,
                    48.858370,
                    userId
            );
            Log.d(TAG, "Sample content 1 added: Eiffel Tower");

            repo.createContent(
                    "Great Wall of China",
                    "https://example.com/great_wall.jpg",
                    "The Great Wall of China is a series of fortifications that were built across the historical northern borders of ancient Chinese states and Imperial China as protection against various nomadic groups from the Eurasian Steppe.",
                    116.570000,
                    40.430000,
                    userId
            );
            Log.d(TAG, "Sample content 2 added: Great Wall of China");

            repo.createContent(
                    "Statue of Liberty",
                    "https://example.com/statue_of_liberty.jpg",
                    "The Statue of Liberty is a colossal neoclassical sculpture on Liberty Island in New York Harbor in New York City, in the United States. The copper statue, a gift from the people of France, was designed by French sculptor Frédéric Auguste Bartholdi.",
                    -74.044500,
                    40.689200,
                    userId
            );
            Log.d(TAG, "Sample content 3 added: Statue of Liberty");

            // Cleanup: Delete the test user (optional, depending on test needs)
            // UserEntity userToDelete = repo.getUserById(userId);
            // if (userToDelete != null) {
            //     repo.deleteUser(userToDelete);
            //     Log.d(TAG, "Test user deleted");
            // }

            Log.d(TAG, "All tests completed successfully");

        } catch (Exception e) {
            Log.e(TAG, "Test failed: " + e.getMessage(), e);
        }
    }
}