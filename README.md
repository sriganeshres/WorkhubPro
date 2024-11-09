Sure, here's an attractive and effective version of the README file with detailed steps, bulletins, commands, and proper structuring:

# WorkhubPro ERP System

## 📱 Setting up the Project

### 🐘 Setting up PostgreSQL

1. First, pull the Docker image of PostgreSQL 15:

```bash
docker pull postgres:15-alpine
```

2. Use the provided Makefile for PostgreSQL operations:

- Initialize the image for local development:

```bash
make postgresinit
```

- Open the PostgreSQL console (psql):

```bash
make postgres
```

- Create the database for users:

```bash
make createdbusers
```

- Drop the users database:

```bash
make dropdbusers
```

- Open the Bash console within the PostgreSQL container:

```bash
make bash
```

3. Using the PostgreSQL console (psql):

- List all available databases:

```sql
root=# \l
```

- Connect to a specific database:

```sql
root=# \c <your_database_name>
```

- Show all relations (tables) in the current database:

```sql
root=# \dt
```

- View all records in a table:

```sql
root=# SELECT * FROM <table_name>;
```

- Quit the PostgreSQL console:

```sql
root=# \q
```

### ⚠️ Troubleshooting PostgreSQL Issues

1. If you encounter an SSL Enabled error:

- Use the Bash console:

```bash
make bash
```

- Navigate to the PostgreSQL data directory:

```bash
f92622037df6:/# cd /var/lib/postgresql/data
```

- Check the SSL setting:

```bash
f92622037df6:/# cat postgresql.conf | grep ssl
```

- If the first line is not `ssl = off`, uncomment the line `# ssl = off`:

```bash
f92622037df6:/# vi postgresql.conf
```

- Demote yourself to a normal user and restart the PostgreSQL service:

```bash
f92622037df6:/# su - postgres
f92622037df6:~$ pg_ctl restart
```

- If the Docker image has stopped running, restart it from Docker Desktop.

- Try using the PostgreSQL service again. It should work now.

## 🚀 Running the Services

### 💬 Chat Service

```bash
cd .\Backend\Chat_service\cmd\
go run main.go
```

### 📋 WorkhubServices

```bash
cd .\Backend\Workhub_service\cmd\
go run main.go
```

### 🔐 Authentication Services

```bash
cd .\Backend\Authentication_service\cmd\
go run main.go
```

### 📲 Android App

1. Open the project in Android Studio.
2. Select an emulator or connect a physical device.
3. Build the Gradle files.
4. Run the app.

## 🔍 Additional Information

- For detailed information about the project, refer to the comprehensive [Software Requirements Specification (SRS) Document](https://github.com/sriganeshres/WorkhubPro/blob/master/Final%20SRS.pdf).
- Check out the project's [GitHub repository](https://github.com/sriganeshres/WorkhubPro) for the latest updates, issues, and pull requests.
- For detailed information about the project, refer to the comprehensive [Software Requirements Specification (SRS) Document](https://github.com/sriganeshres/WorkhubPro/blob/master/Final%20SRS.pdf).
- Explore the project's [Risk Analysis](https://github.com/sriganeshres/WorkhubPro/blob/master/Risk%20Analysis%20and%20Mitigation%20Plan%20.pdf) for in-depth information on architecture, design patterns, and development practices.

## 👥 Project Members

The WorkhubPro ERP System was developed by a dedicated team of contributors:

- [Sri Ganesh Thota](https://github.com/sriganeshres)
- [Aditya Trivedi](https://github.com/adit4443ya)
- [Rahul Reddy](https://github.com/rahulrangers)

Feel free to reach out to any of the project members for more information or collaboration opportunities.
