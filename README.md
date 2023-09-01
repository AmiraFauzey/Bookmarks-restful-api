# Bookmarks-restful-api
Project title: A bookmark project
Project Description
1. This a Bookmark project that has functionalities to create a bookmark of a website that we want to view later
2. This web service has CRUD operations which include:
   a) Create a bookmark, The user can create a new bookmark by entering data such as Bookmark Title, Bookmark URL, Bookmark Description, Bookmark note, Bookmark category, and Bookmark tags
   b) Get bookmark By Bookmark ID
   c) Update a bookmark, The user can update the existing bookmark by editing any fields which are Bookmark Title, Bookmark URL, Bookmark Description, Bookmark note, Bookmark category and Bookmark tags.
   d) Delete a bookmark, The user can delete the bookmark
   e) Users can search the bookmark by Bookmark Title, Category name and Create date 
4. The project is built using MySQL, JPA/Hibernate, Maven and Spring boot
5. To do Searching we have implemented CriteriaBuilder and CriteriaQuery

SQL queries to create tables for the database
1. A Bookmark table
   ```
   CREATE TABLE bookmark_db.BOOKMARK (
    BOOKMARK_ID INT PRIMARY KEY,
    BOOKMARK_TITLE VARCHAR(255),
    BOOKMARK_URL VARCHAR(255),
    BOOKMARK_DESCRIPTION VARCHAR(255),
    BOOKMARK_NOTE VARCHAR(255),
    CATEGORY_ID INT,
    CREATED_DATE TIMESTAMP,
    LAST_MODIFIED_DATE TIMESTAMP,
    FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(CATEGORY_ID)
    );
    ```
2.  A Category table
    ```
    CREATE TABLE bookmark_db.CATEGORY (
     CATEGORY_ID INT PRIMARY KEY,
     CATEGORY_NAME VARCHAR(255)
     );
    ```
4.  A Tag table
    ```
    CREATE TABLE bookmark_db.TAG (
    TAG_ID INT PRIMARY KEY,
    TAG_NAME VARCHAR(255),
    CATEGORY_ID INT,
    FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(CATEGORY_ID)
    );
    ```
    
6. A BookmarkTag table
   ```
   CREATE TABLE bookmark_db.BOOKMARK_TAG (
    BOOKMARK_ID INT,
    TAG_ID INT,
    PRIMARY KEY (BOOKMARK_ID, TAG_ID),
    FOREIGN KEY (BOOKMARK_ID) REFERENCES BOOKMARK(BOOKMARK_ID),
    FOREIGN KEY (TAG_ID) REFERENCES TAG(TAG_ID)
   );
   ```

Test API
1) CREATE BOOKMARK : http://localhost:8080/bookmarks
   Request body:
   ```
     {
       "bookmarkTitle": "My New Bookmark",
       "bookmarkUrl": "https://example.com",
       "description": "This is a description of the bookmark",
       "note": "Here's a note about the bookmark",
       "category": {
           "categoryId": 1,
           "categoryName": "Technology"
       },
       "tags": [
           {
               "tagId": 1,
               "tagName": "Artificial Intelligence"
           },
           {
               "tagId": 2,
               "tagName": "Machine Learning"
           }
       ]
   }
   ```
2. UPDATE BOOKMARK: http://localhost:8080/bookmarks/3?categoryId=1&tagIds=2&tagIds=3
   Request body:
   ```
      {
       "bookmarkTitle": "Introduction to Data Structure",
       "bookmarkUrl": "https://example.com",
       "description": "This is a description of the bookmark",
       "note": "Here's a note about the bookmark",
       "category": {
           "categoryId": 1,
           "categoryName": "Technology"
       },
       "tags": [
           {
               "tagId": 3,
               "tagName": "Data Structures & Algorithms"
           },
           {
               "tagId": 4,
               "tagName": "Complexity Theory"
           }
       ]
   }
   ```
3. Get Bookmark by ID: http://localhost:8080/bookmarks/3
4. Delete Bookmark: http://localhost:8080/bookmarks/5
5. Get all Bookmark: http://localhost:8080/bookmarks/searchResult?pageNumber=0&pageSize=10&sortBy=bookmarkTitle&sortDirection=ASC
6. Search Bookmark by title and category name : http://localhost:8080/bookmarks/searchResult?pageNumber=0&pageSize=10&sortBy=bookmarkTitle&sortDirection=ASC&bookmarkTitle=Introduction&categoryName=Technology
