export const examTypes = ['NSC Final', 'SC']

export const provinces = [
  'Eastern Cape',
  'Free State',
  'Gauteng',
  'KwaZulu-Natal',
  'Limpopo',
  'Mpumalanga',
  'Northern Cape',
  'North West',
  'Western Cape',
]

export const schoolsByProvince = {
  'Eastern Cape': ['Grey High School', 'Selborne College', 'Victoria Park High School'],
  'Free State': ['Grey College', 'St Andrews School', 'Sentraal High School'],
  Gauteng: ['Parktown Boys High School', 'Pretoria Boys High School', 'St Stithians College'],
  'KwaZulu-Natal': [
    'Glenwood High School',
    'Maritzburg College',
    'Westville Senior Primary School',
  ],
  Limpopo: ['Pietersburg High School', 'Merensky High School', 'Hoerskool Ben Vorster'],
  Mpumalanga: ['Hoerskool Nelspruit', 'Rob Ferreira High School', 'Lowveld High School'],
  'Northern Cape': [
    'Kimberley Boys High School',
    'Diamantveld High School',
    "St Cyprian's Grammar School",
  ],
  'North West': ['Potchefstroom Gimnasium', 'Rustenburg Girls High School', 'Hoerskool Rustenburg'],
  'Western Cape': [
    'Wynberg Boys High School',
    'Westerford High School',
    "Rondebosch Boys' High School",
  ],
}

export const graduationYears = Array.from(
  { length: 10 },
  (_, index) => new Date().getFullYear() - index,
)
