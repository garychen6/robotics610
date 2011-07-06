using System;
using System.Windows.Forms;

namespace DCX_Fingerprint_Scanner_CSharp
{
    public partial class frmMain : Form
    {

        FormState formState = new FormState();

        public frmMain()
        {
            InitializeComponent();
        }

        private void btnOpen_Click(object sender, EventArgs e)
        {
            Console.Write("Test\n");
        }

        private void frmMain_Load(object sender, EventArgs e)
        {
            formState.Maximize(this);
        }
    }
}
