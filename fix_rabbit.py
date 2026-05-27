import json
import os

desc_map = {
  "p-curve analysis": "A meta-analytic tool assessing whether a set of significant results contains a true effect; genuine effects produce right-skewed p-value distributions; p-hacked literatures cluster just below .05.",
  "Bayesian factor": "A statistical index expressing the relative evidence for one hypothesis over another; unlike p-values, Bayesian factors can quantify evidence for the null hypothesis.",
  "equivalence testing": "A statistical approach testing whether an effect is smaller than a specified minimum meaningful size, enabling formal inference about the absence of a practically significant effect.",
  "Registered Reports": "A publication format where peer review and in-principle acceptance occur before data collection, removing publication bias; over 300 journals now offer this format.",
  "OSF (Open Science Framework)": "A free open-source platform (osf.io) for registering studies, pre-registering hypotheses, sharing data and materials, and collaborating on research.",
  "GRIM test": "The Granularity-Related Inconsistency of Means test — a mathematical check for whether a reported mean is consistent with the sample size and scale granularity.",
  "specification curve analysis": "A method examining how a statistical result changes across all reasonable analytical decisions, producing a curve of estimates rather than a single point.",
  "multiverse analysis": "A systematic approach examining how results vary across multiple plausible analytical specifications, providing transparent picture of analytical sensitivity.",
  "Ubuntu philosophy": "An African philosophical concept often rendered as 'I am because we are', articulating a relational conception of personhood that challenges Western methodological individualism.",
  "Liberation psychology (Martin-Baro)": "Ignacio Martin-Baro's reorientation of psychology toward the psychological dimensions of oppression and structural violence; asks who psychology serves.",
  "kaupapa Maori research": "An Indigenous research methodology from Aotearoa New Zealand based on Maori values, emphasising community ownership, cultural identity, and reciprocal benefit.",
  "Frantz Fanon": "A Martiniquais psychiatrist and anti-colonial theorist whose work examined the psychological effects of colonialism and racism on colonised peoples.",
  "postcolonial theory": "A body of critical theory examining the cultural, political, and psychological legacies of colonialism; influential in postcolonial psychology and decolonisation.",
  "category fallacy (Kleinman)": "The error of applying a diagnostic category valid in one cultural context to individuals in a different cultural setting without establishing that the construct applies.",
  "community-based participatory research": "A research approach involving community members as partners in all phases of research for reciprocal benefit and community empowerment.",
  "epistemological pluralism": "The position that multiple knowledge systems and epistemological frameworks can legitimately coexist rather than one system having universal dominance.",
  "gender schema theory (Bem)": "Bem's (1981) cognitive theory that children learn to perceive themselves through a gender-based schema; psychological androgyny predicts greater psychological flexibility.",
  "doing gender (West & Zimmerman)": "West and Zimmerman's (1987) concept of gender as enacted and accomplished in social interaction rather than as a fixed attribute of individuals.",
  "gender performativity (Butler)": "Butler's concept that gender is constituted through repeated regulated performances rather than expressing a pre-existing inner essence.",
  "relational-cultural theory": "A psychodynamic framework developed at the Stone Center centring human development in connection rather than separation, reframing women's relational orientation as strength.",
  "feminist empiricism": "A position arguing that more rigorous science eliminating androcentric biases will produce improved knowledge without challenging scientific method's foundations.",
  "standpoint epistemology (Harding)": "Harding's argument that the social position of the knower shapes what can be known, and that marginalised standpoints may provide epistemic advantages.",
  "matrix of domination (Collins)": "Patricia Hill Collins's framework for how multiple systems of power (racism, sexism, classism) operate simultaneously at structural, disciplinary, and interpersonal levels.",
  "stereotype threat in leadership": "The phenomenon whereby awareness of a negative stereotype about one's group activates self-monitoring that impairs performance in leadership contexts.",
  "harmful dysfunction analysis (Wakefield)": "Wakefield's philosophical account requiring two conditions for mental disorder: a mechanism fails its natural function AND this causes socially recognised harm.",
  "medicalisation": "The process by which non-medical conditions are redefined as medical problems; in psychiatry, the expansion of diagnostic categories to encompass ordinary human distress.",
  "anti-psychiatry movement": "A critical movement (Szasz, Laing, Cooper) challenging psychiatric diagnosis, arguing that mental illness is a social construction or response to impossible social situations.",
  "Power Threat Meaning Framework": "A BPS Division of Clinical Psychology framework (Johnstone and Boyle, 2018) proposing an alternative to psychiatric diagnosis centring power, threat, and meaning-making.",
  "Thomas Szasz": "A Hungarian-American psychiatrist whose The Myth of Mental Illness (1961) argued that psychiatric diagnosis medicalises social judgments rather than identifying genuine biological diseases.",
  "R.D. Laing": "A Scottish existential-phenomenological psychiatrist who reframed psychosis as an understandable response to impossible family and social situations.",
  "DSM inflation (Allen Frances)": "Allen Frances's critique arguing DSM-5 lowered thresholds such that ordinary experiences now qualify as psychiatric diagnoses, generating over-diagnosis.",
  "psychiatric survivors movement": "A civil rights and advocacy movement of people who have experienced psychiatric treatment advocating for autonomy, informed consent, and alternatives to coercive care.",
  "qualia": "The subjective phenomenal qualities of experience — what it is like to see red, taste coffee, or feel pain — that Chalmers argues cannot be explained by functional descriptions alone.",
  "philosophical zombie argument": "Chalmers's thought experiment: a being physically identical to a conscious person but lacking subjective experience is conceivable; if so, consciousness is not logically entailed by physical organisation.",
  "explanatory gap (Levine)": "Levine's (1983) concept describing the apparent impossibility of deducing phenomenal facts from physical facts — even a complete description of pain leaves unexplained why it feels bad.",
  "Global Workspace Theory (Dehaene/Baars)": "The theory that consciousness arises when information is broadcast widely across thalamo-cortical and fronto-parietal circuits, becoming available to multiple downstream processes.",
  "Integrated Information Theory (Tononi)": "The theory that consciousness is identical with a system's capacity for integrated information (phi) — the amount of information generated by the system above that generated by its parts.",
  "property dualism": "The metaphysical position that there is one substance but two irreducible property types — physical and phenomenal — that cannot be reduced to each other.",
  "panpsychism (Chalmers/Goff)": "The view that phenomenal properties are fundamental features of reality present at all levels of physical organisation, not emerging at some complexity threshold.",
  "heterophenomenology (Dennett)": "Dennett's methodological approach treating first-person reports as data about beliefs about inner states, not as transparent access to phenomenal properties.",
  "mysterianism (McGinn)": "Colin McGinn's position that the human mind may be architecturally incapable of solving the hard problem — not because there is no solution but because our cognition is limited.",
  "epigenome": "The complete set of epigenetic modifications to an organism's genome — DNA methylation, histone modifications, non-coding RNA — that regulate gene expression without altering DNA sequence.",
  "CpG methylation": "The addition of methyl groups to cytosine residues at CpG sites; typically associated with gene silencing in promoter regions; a primary epigenetic mechanism in mammals.",
  "glucocorticoid receptor gene (NR3C1)": "The gene encoding the glucocorticoid receptor; differential NR3C1 promoter methylation is associated with early adversity and altered stress reactivity in animal and human studies.",
  "missing heritability problem": "The observation that GWAS-identified variants explain only a fraction of twin-study heritability estimates, suggesting complex architecture and gene-environment interactions.",
  "polygenic score": "A composite measure aggregating effects of thousands of genetic variants to estimate genetic predisposition to a trait; increasingly used in gene-environment interaction research.",
  "biological sensitivity to context (Boyce & Ellis)": "The hypothesis that individuals differ in reactivity to environmental conditions — highly sensitive individuals show worse outcomes in adverse but better outcomes in supportive environments.",
  "transgenerational epigenetic inheritance": "Transmission of epigenetic marks from parents to offspring via the germline; established in plants and some animals, limited and contested in humans.",
  "sensitive periods in development": "Developmental windows during which specific experiences have disproportionate and lasting effects on biological and psychological development.",
  "passive vs. active social media use": "The distinction between consuming content without interacting (passive; linked to upward comparison and lower wellbeing) versus active engagement such as messaging and posting.",
  "social comparison theory (Festinger)": "Festinger's (1954) theory that people evaluate opinions and abilities by comparing to others; upward social comparison tends to lower self-evaluation.",
  "displacement hypothesis": "The proposition that social media harms wellbeing by displacing activities (sleep, face-to-face interaction, exercise) that promote wellbeing rather than through direct content effects.",
  "Gaming Disorder (ICD-11)": "A new ICD-11 diagnostic category characterised by impaired control over gaming with priority over other activities; contested as potentially over-pathologising intense gaming.",
  "digital CBT (Woebot, Wysa)": "App-based cognitive behavioural therapy delivered via smartphone; meta-analyses find significant but smaller effects than face-to-face therapy with high dropout rates.",
  "experience sampling methodology": "A research method collecting participants' reports at multiple random time points throughout the day; provides ecologically valid data on daily psychological experience.",
  "ecological momentary assessment": "A research approach using repeated within-day sampling in natural environments; used in digital psychology to capture real-time technology use and wellbeing.",
  "significance quest theory (Kruglanski)": "The motivational theory that radicalisation is driven by a need to matter and have worth, triggered by significance loss and satisfied by extreme group membership.",
  "moral disengagement (Bandura)": "Bandura's concept of cognitive mechanisms — moral justification, dehumanisation, victim blaming — that disengage self-regulatory processes to enable harmful behaviour.",
  "staircase to terrorism model": "Moghaddam's metaphor for the progressive psychological journey from ordinary grievance through increasing psychological distance toward the final step of violence.",
  "group polarisation": "The tendency for group discussion to produce more extreme positions than individual members' pre-discussion views; relevant to online echo chambers and radicalisation.",
  "algorithmic radicalisation": "The hypothesis that recommendation algorithms accelerate radicalisation by surfacing progressively extreme content to engaged users; empirical evidence is mixed.",
  "UK Prevent programme": "A UK counter-terrorism strategy requiring statutory agencies to refer individuals at risk of radicalisation to multi-agency support; criticised for stigmatising Muslim communities.",
  "Channel referrals": "The multi-agency support pathway within Prevent; individuals receive structured assessment and tailored intervention; effectiveness evidence is limited.",
  "exit programmes (deradicalisation)": "Programmes supporting individuals leaving extremist groups through ideological support, practical assistance, and network separation.",
  "treatment gap": "The difference between mental disorder prevalence and proportion receiving treatment; approximately 75% of people with mental disorders in LMICs receive no treatment.",
  "task-shifting (task-sharing)": "Delegation of psychological interventions to less specialised workers with appropriate training; primary WHO strategy for scaling up care in low-resource settings.",
  "Friendship Bench (Chibanda)": "A Zimbabwe-based intervention training community grandmothers to deliver problem-solving therapy on wooden benches; demonstrated effectiveness in RCTs.",
  "Thinking Healthy Programme": "A WHO-endorsed package for perinatal depression in primary care delivered by community health workers; demonstrated effectiveness in Pakistan.",
  "DALYs (disability-adjusted life years)": "A composite measure combining years of life lost to premature mortality and years lived with disability; used by WHO to compare disease burden globally.",
  "idioms of distress": "Culturally specific ways of expressing psychological distress that may not map onto Western psychiatric categories; examples include hwa-byung and ataque de nervios.",
  "Crazy Like Us (Watters)": "Ethan Watters's (2010) book arguing that Western psychiatry exports diagnostic categories to other cultures — a cultural imperialism critique.",
  "WHO mhGAP programme": "The WHO Mental Health Gap Action Programme — evidence-based guidelines for managing mental disorders in non-specialist settings in low- and middle-income countries.",
  "Bonanno trajectory model": "Bonanno's four-trajectory model of adversity response — chronic dysfunction, recovery, delayed reaction, and resilience — showing stable trajectories are most common.",
  "assumptive world theory (Janoff-Bulman)": "The theory that trauma shatters core beliefs about the world's benevolence and meaningfulness, requiring effortful reconstruction — the basis for PTG theory.",
  "Post-Traumatic Growth Inventory (Tedeschi & Calhoun)": "A 21-item measure assessing positive change in five domains: personal strength, new possibilities, relating to others, appreciation for life, and spiritual change.",
  "Penn Resiliency Programme": "A school-based CBT-based resilience intervention for children and adolescents with positive depression prevention effects in multiple RCTs.",
  "Comprehensive Soldier and Family Fitness": "The US Army's large-scale resilience training programme; attracted criticism for insufficient effectiveness evidence at scale.",
  "ordinary magic (Masten)": "Ann Masten's concept that resilience reflects ordinary functioning of common adaptive systems — attachment, self-regulation, social support — not exceptional individual strength.",
  "fear of recurrence (cancer)": "Fear or worry that cancer will return or progress; the most common psychological concern among cancer survivors; responsive to mindfulness-based cognitive therapy.",
  "Dodo Bird verdict (Luborsky)": "Luborsky et al.'s (1975) finding that different bona fide psychotherapies produce broadly equivalent outcomes; supported by Wampold's subsequent meta-analytic work.",
  "therapeutic alliance (Bordin)": "Bordin's tripartite model of the working alliance comprising bond, goals, and tasks; consistently predicts therapy outcomes (r approximately .28).",
  "common factors model (Wampold)": "Wampold's articulation of factors common to all effective therapies — alliance, empathy, expectations, and new learning — as the primary active ingredients.",
  "assimilative integration": "A psychotherapy integration strategy maintaining a primary theoretical orientation while incorporating techniques from other approaches when clinically indicated.",
  "Acceptance and Commitment Therapy (ACT)": "A third-wave CBT using acceptance, defusion, mindfulness, values, and committed action; evidence base across depression, anxiety, chronic pain, and psychosis.",
  "MBCT for recurrent depression": "Mindfulness-Based Cognitive Therapy adapted for relapse prevention in recurrent depression; NICE-recommended for adults with three or more depressive episodes.",
  "psilocybin-assisted therapy": "A psychedelic-assisted psychotherapy using psilocybin with structured preparation, guided sessions, and integration; under Phase 3 investigation for treatment-resistant depression.",
  "precision psychiatry": "Application of genomics, neuroimaging, and machine learning to predict differential treatment response, aiming for individually tailored mental health treatment.",
  "Sackett EBM definition": "Sackett et al.'s (1996) definition of evidence-based medicine as the conscientious, explicit, and judicious use of current best evidence in making care decisions.",
  "evidence hierarchy": "The ranking of evidence types by ability to establish causation — from systematic reviews at the top through RCTs, cohort studies, to expert opinion.",
  "efficacy-effectiveness gap": "The consistent finding that effect sizes in controlled efficacy trials are larger than in real-world effectiveness studies.",
  "Consolidated Framework for Implementation Research (CFIR)": "A framework identifying five domains affecting implementation: intervention characteristics, outer setting, inner setting, individuals, and implementation process.",
  "Knowledge-to-Action cycle": "Graham et al.'s model of knowledge translation as a cyclical process from knowledge creation through adaptation, barrier assessment, implementation, and evaluation.",
  "Rogers' Diffusion of Innovations": "Rogers's framework describing how new practices spread through communities from innovators through the early and late majority to laggards.",
  "pragmatic RCT": "A randomised controlled trial testing whether an intervention works in routine care with typical patients and settings — contrasting with efficacy trials.",
  "Choosing Wisely campaign": "An international initiative encouraging clinicians and patients to question unnecessary tests and treatments using evidence-based criteria.",
  "churnalism": "Journalistic practice of reproducing press releases with minimal independent checking; systematically amplifies overstatements in institutional scientific press releases.",
  "calibrated uncertainty": "Expressing confidence levels that accurately reflect the state of evidence — neither projecting false certainty nor implying that uncertainty makes claims equally unreliable.",
  "science journalism norms": "Professional practices governing science reporting, including source verification, quantitative accuracy, and contextualisation; variably applied under commercial pressure.",
  "power posing controversy (Cuddy)": "The case where Amy Cuddy's power posing research became viral before replication failed; illustrates the costs of premature mass communication of preliminary findings.",
  "deficit model of science communication": "The assumption that public misunderstanding stems from ignorance correctable by information; challenged by evidence that values and trust drive science scepticism.",
  "public engagement vs. outreach": "Outreach is one-way communication from scientists to publics; public engagement involves two-way dialogue where scientists also listen and respond to public concerns.",
  "pre-print servers and media": "Pre-prints posted to servers such as PsyArXiv before peer review are increasingly picked up by media, creating risks of amplifying unreliable findings prematurely.",
  "science Twitter and SciComm norms": "Informal community norms for responsible science communication on social media including transparency about uncertainty and distinguishing findings from opinions.",
  "cultural humility (Tervalon & Murray-Garcia)": "A commitment to ongoing self-reflection, openness to learning from clients, acknowledgment of power differentials, and institutional accountability in cross-cultural practice.",
  "microaggressions (Sue et al.)": "Brief everyday indignities communicating hostile messages to marginalised group members; cumulative exposure is associated with elevated stress, depression, and institutional distrust.",
  "Explanatory Model (Kleinman)": "A clinical interview framework eliciting the patient's own understanding of their illness — its cause, timeline, severity, and appropriate treatment.",
  "Cultural Formulation Interview (DSM-5)": "A 16-item structured interview covering cultural identity, illness explanations, psychosocial stressors, and cultural elements of the clinician-patient relationship.",
  "IAPT equity data": "NHS Talking Therapies monitoring data showing persistent disparities in access and outcomes for Black and minority ethnic groups despite stated aims of equitable provision.",
  "racial disparities in coercive care": "The documented over-representation of Black patients in involuntary psychiatric hospitalisation and restraint in the UK and USA, reflecting structural racism.",
  "ethnic matching in therapy": "Matching clients and therapists from the same ethnic background; evidence on outcome benefits is mixed — cultural humility may matter more than demographic match.",
  "intersectionality in clinical practice": "Attending to how multiple social identities interact to produce specific configurations of experience not captured by addressing each dimension separately.",
  "Innocence Project": "A US non-profit using DNA evidence to exonerate wrongfully convicted people; eyewitness misidentification is the most common factor (~69%) in wrongful convictions.",
  "post-event misinformation effect (Loftus)": "Loftus and Palmer's (1974) demonstration that post-event information can alter eyewitness memory; foundational to forensic psychology.",
  "sequential vs. simultaneous lineups": "Evidence-based reform: sequential lineups (one at a time) produce fewer false identifications than simultaneous lineups while maintaining correct identification rates.",
  "double-blind lineup administration": "A procedure where the administering official does not know who the suspect is, preventing unconscious cuing; a key eyewitness reform.",
  "COMPAS algorithm": "A proprietary recidivism risk assessment algorithm used in US criminal justice; subject to fairness critique showing differential false positive rates by race.",
  "actuarial vs. SPJ risk assessment": "Actuarial tools produce numerical risk scores from statistical weights; Structured Professional Judgment tools use research-derived factors as a framework for expert judgment.",
  "HCR-20": "The Historical Clinical Risk Management-20 — a widely used SPJ tool for violence risk assessment covering historical, clinical, and risk management factors.",
  "recovered memory debate": "A 1990s controversy where therapists claimed to recover repressed abuse memories through suggestion; subsequent research established that suggestion can implant false memories.",
  "algorithmic fairness impossibility theorem": "Chouldechova's proof that when base rates differ across groups, equal calibration and equal false positive/negative rates cannot simultaneously be achieved.",
  "Proctor's tripartite model": "Proctor's (1987) framework identifying three supervision functions: formative (educational), normative (quality assurance), and restorative (supportive).",
  "parallel process in supervision": "Searles's (1955) observation that therapeutic relationship dynamics are sometimes unconsciously replicated in supervision, providing diagnostic information about the therapy.",
  "reflective practitioner (Schon)": "Schon's (1983) model of professional expertise involving reflection-in-action (during practice) and reflection-on-action (deliberate retrospective examination).",
  "deliberate practice (Ericsson)": "Ericsson's framework that expertise requires feedback-rich, challenging practice at the edge of current ability — passive experience accumulation does not produce expertise.",
  "counter-transference": "The therapist's emotional and psychological reactions to the client — a clinical resource when examined in supervision, a potential harm when unexamined.",
  "compassion fatigue": "Reduced empathic capacity and emotional exhaustion from sustained exposure to clients' suffering; distinct from burnout in its specifically empathic component.",
  "secondary traumatic stress": "Indirect exposure to trauma through therapeutic work producing PTSD-like symptoms in the therapist; also called vicarious traumatisation.",
  "HCPC CPD standards": "Health and Care Professions Council requirements mandating sufficient CPD to maintain competence; minimum 150 hours per two-year registration period for psychologists.",
  "BPS Code of Ethics and Conduct": "The British Psychological Society's code organised around four principles: Respect, Competence, Responsibility, and Integrity.",
  "PICOS framework (research question formulation)": "A structured approach specifying Population, Intervention, Comparison, Outcome, and Study design to ensure research questions are specific and answerable.",
  "G*Power software": "Free statistical software for conducting power analyses across a wide range of tests; standard tool for a priori sample size calculation in psychology research.",
  "OSF pre-registration": "Registering hypotheses, design, and analysis plans on osf.io before data collection, creating a time-stamped commitment separating confirmatory from exploratory analyses.",
  "registered reports": "A publication format where peer review and in-principle acceptance occur before data collection, removing publication bias and incentivising null result reporting.",
  "research ethics committee (REC)": "An institutional body reviewing and approving research involving human participants, ensuring compliance with ethical standards for consent, welfare, and data protection.",
  "effect size estimation for power": "Specifying expected effect magnitude for a priori power analysis; based on meta-analytic estimates or minimum meaningful difference rather than single previous studies.",
  "four types of research gap": "Four justifications for original research: empirical (not yet studied), methodological (studied with flawed methods), theoretical (conflicting findings), and practical (not translated).",
  "IPA sampling rationale": "In Interpretative Phenomenological Analysis, justification for purposive small homogeneous samples (typically 6-12) selected for experiential relevance rather than representativeness."
}

path = r'D:\Coding\Real_Projects\arcane-academy\arcane-academy\backend\src\main\resources\content\psychology'
fixed = 0
for fname in sorted(os.listdir(path)):
    if not fname.startswith('psy-lea'):
        continue
    fpath = os.path.join(path, fname)
    with open(fpath, encoding='utf-8') as f:
        data = json.load(f)
    changed = False
    for sc in data.get('subChunks', []):
        terms = sc.get('rabbitHoleTerms', [])
        if terms and isinstance(terms[0], str):
            new_terms = []
            for t in terms:
                desc = None
                for k, v in desc_map.items():
                    if k.lower() == t.lower():
                        desc = v
                        break
                if desc is None:
                    # Try partial match
                    t_lower = t.lower()
                    for k, v in desc_map.items():
                        if t_lower in k.lower() or k.lower() in t_lower:
                            desc = v
                            break
                if desc is None:
                    desc = f"An important concept in {data['title']} — explore further through the rabbit hole terms."
                new_terms.append({"term": t, "description": desc})
            sc['rabbitHoleTerms'] = new_terms
            changed = True
    if changed:
        with open(fpath, 'w', encoding='utf-8') as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
        fixed += 1
        print(f"Fixed: {fname}")

print(f"Total fixed: {fixed}")
