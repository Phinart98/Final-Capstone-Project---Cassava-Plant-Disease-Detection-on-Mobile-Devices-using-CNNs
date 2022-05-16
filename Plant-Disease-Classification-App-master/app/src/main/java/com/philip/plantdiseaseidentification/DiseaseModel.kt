package com.philip.plantdiseaseidentification

data class DiseaseModel(
    var entryId: String? = null,
    var sendTimeAndDate: String? = null,
//    var farmerName: String? = null,
//    var location: String? = null,
    var diseaseType: String? = null,
    var confidence: String? = null
)