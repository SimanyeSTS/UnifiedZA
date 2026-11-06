// src/data/institutionsData.js
export const institutions = [
  {
    id: 'cput',
    institutionID: 1,
    name: 'Cape Peninsula University of Technology',
    shortName: 'CPUT',
    province: 'Western Cape',
    city: 'Cape Town',
    type: 'University of Technology',
    aps: 26,
    description: 'Career-focused education, applied research and innovation across technology, business, design and applied sciences.',
    website: 'https://www.cput.ac.za',
    campuses: ['Cape Town Campus', 'Bellville Campus', 'Mowbray Campus', 'Wellington Campus'],
    courses: [
      { courseID: 1, name: 'Diploma in Information Technology', faculty: 'Informatics and Design', aps: 28, requirements: 'Mathematics or Mathematical Literacy' },
      { courseID: 2, name: 'Diploma in Multimedia Design', faculty: 'Informatics and Design', aps: 26, requirements: 'English' },
      { courseID: 3, name: 'Diploma in Applications Development', faculty: 'Informatics and Design', aps: 28, requirements: 'Mathematics' },
      { courseID: 4, name: 'Diploma in Business Administration', faculty: 'Business and Management Sciences', aps: 26, requirements: 'English' }
    ]
  },
  {
    id: 'uct',
    institutionID: 2,
    name: 'University of Cape Town',
    shortName: 'UCT',
    province: 'Western Cape',
    city: 'Cape Town',
    type: 'Traditional University',
    aps: 32,
    description: 'A research-intensive university with a broad range of undergraduate programmes.',
    website: 'https://www.uct.ac.za',
    campuses: ['Upper Campus', 'Middle Campus', 'Lower Campus', 'Health Sciences Campus'],
    courses: [
      { courseID: 5, name: 'BCom', faculty: 'Commerce', aps: 36, requirements: 'Mathematics' },
      { courseID: 6, name: 'BSc Computer Science', faculty: 'Science', aps: 36, requirements: 'Mathematics' },
      { courseID: 7, name: 'BEng', faculty: 'Engineering and the Built Environment', aps: 40, requirements: 'Mathematics and Physical Sciences' },
      { courseID: 8, name: 'BA', faculty: 'Humanities', aps: 34, requirements: 'English' }
    ]
  },
  {
    id: 'uwc',
    institutionID: 3,
    name: 'University of the Western Cape',
    shortName: 'UWC',
    province: 'Western Cape',
    city: 'Bellville',
    type: 'Traditional University',
    aps: 28,
    description: 'A public university committed to accessible education, research and community engagement.',
    website: 'https://www.uwc.ac.za',
    campuses: ['Bellville Main Campus'],
    courses: [
      { courseID: 9, name: 'BSc Computer Science', faculty: 'Natural Sciences', aps: 30, requirements: 'Mathematics' },
      { courseID: 10, name: 'BCom', faculty: 'Economic and Management Sciences', aps: 28, requirements: 'Mathematics or Mathematical Literacy' },
      { courseID: 11, name: 'LLB', faculty: 'Law', aps: 32, requirements: 'English' },
      { courseID: 12, name: 'BEd', faculty: 'Education', aps: 28, requirements: 'English' }
    ]
  },
  {
    id: 'sun',
    institutionID: 4,
    name: 'Stellenbosch University',
    shortName: 'SU',
    province: 'Western Cape',
    city: 'Stellenbosch',
    type: 'Traditional University',
    aps: 30,
    description: 'A comprehensive university offering programmes across science, commerce, engineering and humanities.',
    website: 'https://www.sun.ac.za',
    campuses: ['Stellenbosch Campus', 'Tygerberg Campus', 'Bellville Park Campus', 'Saldanha Campus'],
    courses: [
      { courseID: 13, name: 'BCom', faculty: 'Economic and Management Sciences', aps: 34, requirements: 'Mathematics' },
      { courseID: 14, name: 'BSc', faculty: 'Science', aps: 34, requirements: 'Mathematics and Physical Sciences' },
      { courseID: 15, name: 'BEng', faculty: 'Engineering', aps: 40, requirements: 'Mathematics and Physical Sciences' }
    ]
  },
  {
    id: 'up',
    institutionID: 5,
    name: 'University of Pretoria',
    shortName: 'UP',
    province: 'Gauteng',
    city: 'Pretoria',
    type: 'Traditional University',
    aps: 30,
    description: 'A large public university with a diverse range of undergraduate and postgraduate programmes.',
    website: 'https://www.up.ac.za',
    campuses: ['Hatfield Campus', 'Groenkloof Campus', 'Prinshof Campus'],
    courses: [
      { courseID: 16, name: 'BSc Computer Science', faculty: 'Engineering, Built Environment and IT', aps: 34, requirements: 'Mathematics' },
      { courseID: 17, name: 'BCom', faculty: 'Economic and Management Sciences', aps: 32, requirements: 'Mathematics' },
      { courseID: 18, name: 'BEng', faculty: 'Engineering, Built Environment and IT', aps: 35, requirements: 'Mathematics and Physical Sciences' }
    ]
  },
  {
    id: 'ufs',
    institutionID: 6,
    name: 'University of the Free State',
    shortName: 'UFS',
    province: 'Free State',
    city: 'Bloemfontein',
    type: 'Traditional University',
    aps: 28,
    description: 'A public university with campuses and programmes serving a diverse student community.',
    website: 'https://www.ufs.ac.za',
    campuses: ['Bloemfontein Campus', 'Qwaqwa Campus', 'South Campus'],
    courses: [
      { courseID: 19, name: 'BSc', faculty: 'Natural and Agricultural Sciences', aps: 30, requirements: 'Mathematics' },
      { courseID: 20, name: 'BCom', faculty: 'Economic and Management Sciences', aps: 28, requirements: 'Mathematics or Mathematical Literacy' },
      { courseID: 21, name: 'BA', faculty: 'Humanities', aps: 28, requirements: 'English' }
    ]
  }
]

export const allCoursesFlat = institutions.flatMap(inst =>
  inst.courses.map(course => ({
    ...course,
    institutionID: inst.institutionID,
    institutionName: inst.name,
    institutionShortName: inst.shortName,
    province: inst.province,
    city: inst.city
  }))
)
