CheckMates

 

Effortless attendance tracking for professors and students—powered by QR codes, AI assistance, and campus‑wide security.

🚀 Features

One‑Click QR Generation: Instantly generate a unique QR code for each class session.

Mobile‑First Check‑In: Students scan the QR code, enter a short passcode, and their attendance is recorded in seconds.

AI Assistant “Saki”: Automated reminders for late or missing check‑ins, pattern analysis, and visual attendance reports.

IP Restriction Mode: Enforce check‑ins only from on‑campus IP ranges—VPNs and off‑site connections are blocked.

Intuitive Dashboard: Clean, user‑friendly UI/UX for professors to manage sessions, view summaries, and export data.

🎬 Demo



⚙️ Getting Started

Prerequisites

Java 11+

PHP 7.4+ with cURL enabled

MySQL or PostgreSQL database

Node.js 14+ (for optional front‑end build)

Installation

Clone the repo

git clone https://github.com/your-org/checkmates.git
cd checkmates

Backend setup

Copy .env.example to .env and configure your database and campus IP CIDRs.

Run migrations:

php artisan migrate

Seed sample data (optional):

php artisan db:seed

Frontend setup (if separate UI)

cd frontend
npm install
npm run build

Serve the application

php artisan serve  # runs on http://localhost:8000

👩‍🏫 Usage

For Professors

Log in to the CheckMates dashboard.

Create or select a class session.

Click Generate QR Code—the code appears on‑screen.

Share the session passcode with students.

Monitor live check‑ins, view attendance heatmaps, and export CSV/PDF reports.

For Students

Open your phone’s camera and scan the on‑screen QR code.

Enter the passcode on the external form.

Click Submit—you’ll see a confirmation message.

🔒 IP Restriction Mode

Enable IP Restriction in Settings to whitelist your campus’s public CIDR ranges. All check‑ins outside these ranges—including VPN tunnels—are automatically denied:

function isOnCampus(string $ip): bool {
    $campusCidrs = ['203.0.113.0/24', '198.51.100.0/24'];
    foreach ($campusCidrs as $cidr) {
        if (ipInRange($ip, $cidr)) return true;
    }
    return false;
}

📦 API Reference

Endpoint

Method

Description

/api/sessions

GET

List all sessions

/api/sessions/{id}

GET

Get session details

/api/checkin

POST

Submit attendance (requires QR + passcode)

/api/reports/{session}

GET

Download attendance report (CSV/PDF)

🤝 Contributing

Fork the repo

Create a feature branch (git checkout -b feature/YourFeature)

Commit your changes (git commit -m 'Add awesome feature')

Push to the branch (git push origin feature/YourFeature)

Open a Pull Request

Please adhere to the Code of Conduct and ensure all new features include tests.

📝 License

This project is licensed under the MIT License.

📬 Contact

Maintainers: questions@cm8tes.com

