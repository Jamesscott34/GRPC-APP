# GRPC Android App - Setup & Migration Guide

## Overview
This project has been sanitized to remove all sensitive and personal data. All user-specific names, emails, and mobile numbers have been replaced with generic placeholders. The app is now ready for safe sharing and further development.

---

## App Description

### What This App Does
The GRPC (Good Riddance Pest Control) Android app is a comprehensive pest control management system designed for pest control companies and technicians. It provides a complete solution for managing pest control operations, from lead generation to job completion and reporting.

### Key Features

#### 1. **Lead Management**
- Generate and track new leads from potential customers
- Store customer details, premises information, and pricing quotes
- Categorize leads as "Job" or "Contract" opportunities
- Automatic commission calculation for contract leads (10%)
- Lead assignment and status tracking

#### 2. **Contract Management**
- Create and manage pest control service contracts
- Track contract visits and maintenance schedules
- Monitor contract status (behind, due, up-to-date)
- Generate contract reports and documentation
- WhatsApp notifications for overdue contracts

#### 3. **Job Management**
- Create and assign pest control jobs to technicians
- Track job status and completion
- Add payment details and materials costs
- Generate job reports and invoices
- WhatsApp notifications for job assignments

#### 4. **Report Generation**
- Generate comprehensive PDF reports for all services
- Multiple report types: Routine, Initial Setup, Call Out, etc.
- Professional formatting with company branding
- Automatic date tracking and technician signatures
- Report storage and retrieval system

#### 5. **Quotation System**
- Generate professional pest control quotations
- Multiple service packages (4pt, 6pt, 8pt, 12pt contracts)
- Automatic VAT calculations and payment schedules
- PDF quotation generation with company details
- Quote tracking and management

#### 6. **Technician Management**
- Assign jobs and contracts to technicians
- Track technician performance and workload
- WhatsApp integration for job notifications
- Technician contact management

#### 7. **Document Management**
- Store and organize all pest control documentation
- PDF generation for reports, quotes, and contracts
- File management system for easy access
- Backup and restore functionality

### Target Users
- Pest control companies
- Pest control technicians
- Service managers
- Administrative staff

---

## Files Changed (Sanitization)
The following files were changed to remove or genericize sensitive data:

- `grpc/app/src/main/java/com/grpc/grpc/General4ptActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/General6ptActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/General8ptActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/General12ptActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/ViewJobActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/ViewLeadsActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/ViewContractActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/AddContractActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/GenerateLeadsActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/MainActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentRoutineActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentJobActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentInitialActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentCallOutExternalActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentCallOutActivity.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentActivityRoutine.java`
- `grpc/app/src/main/java/com/grpc/grpc/RodentActivityExternalRoutine.java`

**What was changed:**
- All personal names replaced with `user` or generic terms.
- All email addresses replaced with `user@useremail.com`.
- All mobile numbers replaced with `mobile`.
- All admin logic now uses `user` as the admin.
- No hardcoded sensitive data remains.

---

## Setup Instructions

### 1. Clone the Repository
```sh
git clone https://github.com/Jamesscott34/GRPC-APP.git
cd GRPC-APP
```

### 2. Install Android Studio & SDK
- Download and install [Android Studio](https://developer.android.com/studio)
- Open the project in Android Studio
- Let it sync and download all dependencies
- Ensure you have the latest Android SDK installed

### 3. Restore Firebase Configuration
- Download your `google-services.json` from the [Firebase Console](https://console.firebase.google.com/)
- Place it in `grpc/app/google-services.json`
- Make sure your Firebase project has the following services enabled:
  - Authentication (Email/Password)
  - Firestore Database
  - Storage
  - Cloud Functions (optional)

### 4. Add Custom Images and Branding

#### Adding Company Logo
1. Navigate to `grpc/app/src/main/res/drawable/`
2. Add your company logo as `logo.png` (or `logo.jpg`)
3. Recommended size: 200x200 pixels or larger
4. The app will automatically use this logo in all PDF reports and quotations

#### Adding Other Images
1. Place any additional images in the `drawable` folder
2. Use descriptive names (e.g., `company_banner.png`, `signature.png`)
3. Supported formats: PNG, JPG, WebP
4. Reference images in code using: `R.drawable.your_image_name`

#### Image Guidelines
- **Logo**: Use PNG format for best quality, especially if it has transparency
- **Size**: Keep images under 1MB for optimal app performance
- **Naming**: Use lowercase letters and underscores (e.g., `company_logo.png`)
- **Resolution**: Provide high-resolution images for crisp display on all devices

### 5. Customize App Branding
Update the following files to reflect your company:
- `grpc/app/src/main/res/values/strings.xml` - App name and text
- `grpc/app/src/main/res/values/colors.xml` - Brand colors
- `grpc/app/src/main/res/values/themes.xml` - App theme

### 6. (Optional) Restore Firebase Functions
If you use Firebase Cloud Functions:
- Go to the `functions/` directory
- Run `npm install` to install dependencies
- Deploy with `firebase deploy --only functions` (requires Firebase CLI)

### 7. Build & Run the App
- Use Android Studio to build and run the app on an emulator or device
- Make sure your Firebase project is set up for Authentication, Firestore/Realtime Database, and Storage as needed
- Test all major features: lead generation, contract management, job tracking, and report generation

### 8. (Optional) Update Environment/Secrets
- If you use `.env` or other secret files, restore them as needed (these are ignored by git)
- Update any API keys or configuration files specific to your deployment

---

## App Structure

### Main Activities
- **MainActivity**: Dashboard and navigation hub
- **LoginActivity**: User authentication
- **GenerateLeadsActivity**: Create new leads
- **ViewLeadsActivity**: Manage and view leads
- **AddContractActivity**: Create new contracts
- **ViewContractActivity**: Manage contracts and visits
- **ViewJobActivity**: Manage pest control jobs
- **Report Activities**: Generate various types of reports

### Key Features by Module
- **Lead Management**: Customer acquisition and tracking
- **Contract Management**: Service agreement management
- **Job Management**: Work order and task management
- **Reporting**: PDF generation and document management
- **Quotations**: Professional quote generation
- **Notifications**: WhatsApp integration for alerts

---

## Firebase Setup Requirements

### Required Services
1. **Authentication**
   - Enable Email/Password authentication
   - Set up user accounts for your team

2. **Firestore Database**
   - Collections: Leads, Contracts, Jobs, Reports
   - Security rules for data access

3. **Storage**
   - For storing PDF reports and documents
   - Configure security rules

### Optional Services
- **Cloud Functions**: For advanced automation
- **Cloud Messaging**: For push notifications
- **Analytics**: For usage tracking

---

## Notes
- All sensitive data has been removed. You must provide your own Firebase configuration and any other secrets required for your deployment.
- If you add new secrets or config files, update your `.gitignore` to keep them out of version control.
- The app is designed to be scalable and can handle multiple technicians and large numbers of contracts/jobs.
- Regular backups of your Firebase data are recommended.

---

## Support
**If you have any issues or need further setup help, please consult the Android Studio and Firebase documentation, or contact the project maintainer.**

### Common Issues
- **Build errors**: Make sure all dependencies are synced in Android Studio
- **Firebase connection**: Verify your `google-services.json` is correctly placed
- **Image not showing**: Check that images are in the correct `drawable` folder
- **PDF generation**: Ensure you have proper permissions for file writing

---

**Version**: 2.1  
**Last Updated**: [Current Date]  
**Compatibility**: Android API 27+ (Android 8.1+)
