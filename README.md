# MusicBandTeam - Online Event Booking Application

## Introduction

Welcome to the MusicBandTeam's online event booking application repository. Developed by passionate students from the University of Melbourne, this platform aims to redefine how music enthusiasts in Australia book live events. Inspired by the likes of Ticketek, we're here to make the event booking process efficient and hassle-free.

## SWEN90007 Project - Part 2 Deliverable

This part focuses on the design and implementation of our app based on the Application Domain and the use cases from Part 1. The application is designed considering single-user access, thus avoiding concurrent issues for now.

Incorporated Patterns:
- Domain model
- Data mapper
- Unit of work
- Lazy load
- Identity field
- Foreign key mapping
- Association table mapping
- Embedded value
- Inheritance patterns (Specific pattern used: [Specify the inheritance pattern you've used])
- Authentication and Authorization (using [mention the library/framework])

## Deployment

- **Frontend**: [Render Frontend Deployment](https://musicbandteam-frontend.onrender.com)
- **Backend**: [Render Backend Deployment](https://event-backend-e7yb.onrender.com/event-backend)

## Repository Structure

```
├── docs/                   # Documentation (Project reports, minutes, and data samples)
│   ├── part1/              # Part 1 report
│   ├── part2/              # Part 2 report - Software Architecture Design (SAD)
│   ├── part3/              # Part 3 report
│   ├── part4/              # Part 4 report
│   └── data-samples/       # Data samples and test case instructions
├── src/                    # Source code
└── README.md               # Project README file
```

## Database Samples

To simplify testing for our stakeholders, our deployed app includes a populated database filled with realistic data samples essential for testing the system. For a detailed breakdown of the data and instructions on how to use it, refer to our [Data Samples Document](docs/data-samples/Data_Samples_Document.md) within the `docs/data-samples/` directory.

- **docs/**: This directory houses all the project documentation, including reports and meeting minutes. We have organized it into subfolders for each part of the project to maintain clarity and organization.

  - **part1/**: Part 1 report, which includes the project overview, application domain, use case diagram, and simplified use case descriptions.

  - **part2/**: Part 2 report, which covers the architectural design, system components, and technology stack.

  - **part3/**: Part 3 report, containing the implementation details, challenges faced, and solutions applied.

  - **part4/**: Part 4 report, showcasing the testing procedures, test results, and overall project evaluation.

  - **data-samples/**: This subfolder contains the necessary data samples (inputs) used to simulate and demonstrate our application's functionalities during testing and development.

- **src/**: This directory contains the source code of our online event booking application. It comprises various modules, components, and configurations required for the application's functionality.

- **README.md**: This is the main README file you are currently reading. It provides an overview of the repository structure, introduces the project, and serves as a guide for contributors and users.

We hope you find our project repository organized and user-friendly. If you have any questions or feedback, feel free to reach out to us. Thank you for your interest in our Music Events System project!

## Part 3 - Concurrency Management

As a part of our SWEN90007 project, this section deals with concurrency issues that arise due to multiple users interacting with the application simultaneously. Some of the challenges include:

- Multiple customers trying to purchase tickets to the same event simultaneously.
- Multiple event planners managing the same event.

### Concurrency Solutions

* **Issue 1: Multiple Ticket Bookings**  
  * Solution: Optimistic Online Locks  
  * Testing: Simulated multiple users trying to purchase the same ticket.

* **Issue 2: Multiple Event Management**  
  * Solution: Optimistic Online Locks  
  * Testing: Simulated multiple event planners editing the same event.

* **Issue 3: Multiple Venue Editing Management**  
  * Solution: Permistic Online Locks  
  * Testing: Simulated one admin view and editing the same venue.

A comprehensive report detailing each concurrency issue, our chosen solutions, sequence diagrams, and test results can be found in `docs/part3`.

## Deployment Links

- **Frontend**: [Click Here](https://musicbandteam-frontend.onrender.com)
- **Backend**: [Click Here](https://event-backend-e7yb.onrender.com/event-backend)

## Testing

For the third phase of our project, we have tagged our repository with `SWEN90007_2023_Part3_MusicBandTeam`. Use this tag to review the specific state of the project for this deliverable.

### Preloaded Database

Our application includes a preloaded database with realistic data samples essential for testing. To understand our data structure and samples, please refer to `docs/data-samples`.

**Credentials**:  
- **Administrator**:  
  - Username: admin@email.com
  - Password: admin

Detailed data samples can be found [here](docs/data-samples).

## Getting Started

1. Clone this repository.
2. Navigate to the `frontend` directory.
3. Run `npm install` to install frontend dependencies.
4. Run `npm start` to start the frontend development server.

