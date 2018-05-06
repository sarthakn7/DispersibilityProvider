create KEYSPACE dispersibility WITH replication = {'class': 'SimpleStrategy', 'replication_factor': 1};

create table dispersibility.apps (
  service text PRIMARY KEY,
  app_class_name text,
  jar_file_name text,
  jar blob,
);