# WiFi Shivpreet Creations — RADIUS Manager (Next.js)

Next.js + MariaDB RADIUS management panel.

## Admin bootstrap

Default requested administrator username: `admin`

For security, the password is not stored in Git. Create/update the account on the server with:

```bash
ADMIN_USERNAME=admin ADMIN_PASSWORD='pass@121' node scripts/create-admin.mjs
```

Then sign in at `/login`.

The bootstrap script stores only a bcrypt password hash in MariaDB.
