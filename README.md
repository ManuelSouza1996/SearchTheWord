# SoftwareEngineering

Hey guys & gals, we can use this README file to hold important information, such as how to use Git.

If you are using MacOS or a Linux distribution, you can use Git right out of the box. If you have a Windows machine, you need to install some extra software that will enable you to use Git.

Git For Windows appears to be the best option and is easy to install:

        https://gitforwindows.org

Brief rundown of how to "git" (haha) the files from the repository:

        1. You need a GitHub account.
        2. Run the following commands to set your email and username
                git config --global user.email "the email you used to signup"
                git config --global user.name "your username"
                *INCLUDE THE QUOTATION MARKS*
        3. Navigate in the terminal to where you want to work with the files.
        4. Run:
                git init
        5. Run:
                git clone https://github.com/burkerking72/DB
        6. You now have the "master" version of the files. You can edit these files in any way, and the files on github will not be           affected. This allows you to create your own branch, that you can later "push" to github to update the files on the repository.
        7. When you are ready to add files to the repository you must first "add" them.
                git add "the file you want to add"
        8. When you have added all of the files you want to add, you must make a commit.
                git commit -m "Message saying what you're adding"
        9. When all is said and done, you may "push" it to the final branch.
                git push -u origin master

TADA! Git basics done.
