package com.lagoela.notfan_backend.file;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lagoela.notfan_backend.json.FollowersJsonStructure;
import com.lagoela.notfan_backend.json.FollowingJsonStructure;
import com.lagoela.notfan_backend.json.FollowingModel;
import com.lagoela.notfan_backend.json.FollowingNotFollowingModel;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Service
public class FileService {

    public List<FollowingNotFollowingModel> processZip(MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            throw new IOException("File is empty");
        }

        if(!Objects.equals(file.getContentType(), "application/zip")) {
            throw new IOException("File is not a zip");
        }
        Map<String, Path> jsonFiles = unzipFile(file);

        List<FollowingNotFollowingModel> followingNotFollowing = new ArrayList<FollowingNotFollowingModel>();

        Map<String, String> followersList = processFollowersJson(jsonFiles.get("followers"));
        List<FollowingModel> followingList = processFollowingJson(jsonFiles.get("following"));

        for (FollowingModel followingUser : followingList) {
            if (!followersList.containsKey(followingUser.getUserNickname())) {
                FollowingNotFollowingModel notFollowingUser = FollowingNotFollowingModel.builder()
                        .userNickname(followingUser.getUserNickname())
                        .profileLink(followingUser.getProfileLink()).build();

                followingNotFollowing.add(notFollowingUser);
            }
        }

        return followingNotFollowing;
    }

    private Map<String, Path> unzipFile(MultipartFile zip) throws IOException {
        // Getting temp folder path for creating zip file
        Path workingDir = Paths.get(System.getProperty("user.dir"));

        String userName = Objects.requireNonNull(zip.getOriginalFilename().split("-")[1]);
        System.out.println(userName);

        Path zipTempFolder = workingDir.resolve("src")
                .resolve("main")
                .resolve("resources")
                .resolve("temp")
                .resolve(userName);

        if(!Files.exists(zipTempFolder)) {
            try {
                Files.createDirectory(zipTempFolder);
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        if(!Files.isDirectory(zipTempFolder)) {
            try {
                if(Files.deleteIfExists(zipTempFolder)) {
                    Files.createDirectory(zipTempFolder);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        Path zipPath = zipTempFolder.resolve(Objects.requireNonNull(zip.getOriginalFilename()));
        try {
            Files.copy(zip.getInputStream(), zipPath);
        } catch (FileAlreadyExistsException e) {
            try {
                Files.deleteIfExists(zipPath);
                Files.copy(zip.getInputStream(), zipPath);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path followersPath;
        Path followingPath;

        try (ZipFile zipFile = new ZipFile(new File(zipPath.toUri()))) {
            Enumeration<? extends ZipEntry> zipEntries = zipFile.entries();
            String followersZipPath = "connections/followers_and_following/followers_1.json";
            String followingZipPath = "connections/followers_and_following/following.json";

            followersPath = getFollowersJson(zipFile.getInputStream(zipFile.getEntry(followersZipPath)), zipTempFolder);
            followingPath = getFollowingJson(zipFile.getInputStream(zipFile.getEntry(followingZipPath)), zipTempFolder);


        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

        return Map.ofEntries(
                Map.entry("followers", followersPath),
                Map.entry("following", followingPath)
        );
    }

    private Path getFollowersJson(InputStream jsonFile, Path tempPath) throws IOException {
        Path jsonPath = tempPath.resolve("followers.json");

        try {
            Files.deleteIfExists(jsonPath);
            Files.copy(jsonFile, jsonPath);
        } catch (IOException e) {
            throw new IOException(e);
        }

        return jsonPath;
    }
    private Path getFollowingJson(InputStream jsonFile, Path tempPath) throws IOException {
        Path jsonPath = tempPath.resolve("following.json");

        try {
            Files.deleteIfExists(jsonPath);
            Files.copy(jsonFile, jsonPath);
        } catch (IOException e) {
            throw new IOException(e);
        }

        return jsonPath;
    }

    private Map<String, String> processFollowersJson(Path jsonPath) throws IOException {
        Map<String, String> followers = new HashMap<String, String>();
        try {
            Gson gson = new Gson();
            Reader reader;
            try {
                reader = Files.newBufferedReader(jsonPath);
            } catch (IOException e) {
                throw new IOException(e);
            }

            Type listType = new TypeToken<List<FollowersJsonStructure>>() {}.getType();
            List<FollowersJsonStructure> followersJsonList = gson.fromJson(reader, listType);
            for (FollowersJsonStructure follower : followersJsonList) {
                followers.put(
                        follower.getString_list_data().getFirst().getValue(),
                        follower.getString_list_data().getFirst().getHref()
                );
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return followers;
    }

    private List<FollowingModel> processFollowingJson(Path jsonPath) throws IOException {
        List<FollowingModel> following = new ArrayList<FollowingModel>();

        try {
            Gson gson = new Gson();
            Reader reader;
            try {
                reader = Files.newBufferedReader(jsonPath);
            } catch (IOException e) {
                throw new IOException(e);
            }

            FollowingJsonStructure followingJson = gson.fromJson(reader, FollowingJsonStructure.class);
            for (FollowingJsonStructure.relationships_following followingUser : followingJson.getRelationships_following()) {
                FollowingModel userFollowing = FollowingModel.builder()
                        .userNickname(followingUser.getTitle())
                        .profileLink(followingUser.getString_list_data().getFirst().getHref())
                        .build();
                following.add(userFollowing);
            };

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return following;
    }
}
