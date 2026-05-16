# Unified ZA – Student Portal

A React student portal for tracking university applications in South Africa.

## Features

- **Dashboard** – Overview of total, pending, and accepted applications plus saved courses
- **My Applications** – Full table view of all submitted applications with status badges
- **Saved Courses** – Application cart (max 3 courses) with add/remove functionality
- **Status Tracker** – Visual timeline showing the progress of each application
- **Application Details** – Read-only view of submitted application information

## Tech Stack

| Tool | Purpose |
|------|---------|
| React 18 | UI framework |
| React Router v6 | Client-side routing |
| Vite | Build tool / dev server |
| CSS Custom Properties | Theming & design tokens |

## Getting Started

### Prerequisites
- Node.js 18+
- npm or yarn

### Install & Run

```bash
git clone https://github.com/YOUR_USERNAME/unified-za-portal.git
cd unified-za-portal

npm install

npm run dev
```

The app will be available at `http://localhost:5173`

### Build for Production

```bash
npm run build
npm run preview
```

## Project Structure

```
unified-za-portal/
├── index.html
├── vite.config.js
├── package.json
├── README.md
└── src/
    ├── main.jsx              # Entry point
    ├── App.jsx               # Routes
    ├── index.css             # Global styles & design tokens
    ├── components/
    │   ├── Sidebar.jsx       # Navigation sidebar
    │   ├── Topbar.jsx        # Search & notifications bar
    │   └── StatusBadge.jsx   # Reusable status pill
    ├── pages/
    │   ├── Dashboard.jsx
    │   ├── MyApplications.jsx
    │   ├── SavedCourses.jsx
    │   ├── StatusTracker.jsx
    │   ├── ApplicationDetails.jsx
    │   └── Placeholders.jsx  # Settings & Support stubs
    └── data/
        └── mockData.js       # Static mock data
```

## Screens

| Route | Screen |
|-------|--------|
| `/` | Dashboard |
| `/my-applications` | My Applications |
| `/my-applications/:id` | Application Details |
| `/saved-courses` | Saved Courses (Cart) |
| `/status-tracker` | Status Tracker (default: first app) |
| `/status-tracker/:id` | Status Tracker (specific app) |

## Assignment Notes

- Mock data lives in `src/data/mockData.js` — edit it to add more applications or courses
- Design tokens are in `src/index.css` under `:root` — change colours there globally
- The `SavedCourses` page uses local React state for add/remove; no backend needed

## License

MIT
